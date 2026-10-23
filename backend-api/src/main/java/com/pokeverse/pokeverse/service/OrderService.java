
package com.pokeverse.pokeverse.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.pokeverse.pokeverse.dto.OrderItemResponse;
import com.pokeverse.pokeverse.dto.OrderResponse;
import com.pokeverse.pokeverse.entity.CardListing;
import com.pokeverse.pokeverse.entity.Customer;
import com.pokeverse.pokeverse.entity.Order;
import com.pokeverse.pokeverse.entity.OrderItem;
import com.pokeverse.pokeverse.repository.CardListingRepository;
import com.pokeverse.pokeverse.repository.CustomerRepository;
import com.pokeverse.pokeverse.repository.OrderItemRepository;
import com.pokeverse.pokeverse.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final CardListingRepository cardListingRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CustomerRepository customerRepository,
            CardListingRepository cardListingRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.cardListingRepository = cardListingRepository;
    }

    public record OrderLine(
            Long cardListingId,
            Integer quantity
    ) {
    }

    @Transactional
    public OrderResponse placeOrder(
            Long customerId,
            List<OrderLine> items) {

        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Order must contain at least one item"
            );
        }

        for (OrderLine item : items) {
            if (item == null
                    || item.cardListingId() == null
                    || item.quantity() == null
                    || item.quantity() <= 0) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid order item"
                );
            }
        }

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found"
                ));

        // Lock cards in consistent order to reduce
        // the possibility of database deadlocks.
        List<OrderLine> sortedItems = new ArrayList<>(items);
        sortedItems.sort(
                Comparator.comparing(OrderLine::cardListingId)
        );

        Order order = new Order();
        order.setCustomer(customer);
        order = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderLine requestedItem : sortedItems) {

            CardListing card = cardListingRepository
                    .findByIdForUpdate(requestedItem.cardListingId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Card not found"
                    ));

            int quantity = requestedItem.quantity();

            if (card.getStock() == null
                    || card.getStock() < quantity) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Not enough stock"
                );
            }

            if (card.getPrice() == null
                    || card.getPrice().signum() < 0) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Invalid card price"
                );
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setCardListing(card);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(card.getPrice());

            orderItemRepository.save(orderItem);

            BigDecimal subtotal = card.getPrice()
                    .multiply(BigDecimal.valueOf(quantity));

            total = total.add(subtotal);

            card.setStock(card.getStock() - quantity);
            cardListingRepository.save(card);
        }

        order.setTotalAmount(total);
        order = orderRepository.save(order);

        return buildResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getPurchaseHistory(
            Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found"
            );
        }

        return orderRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::buildResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found"
                ));

        return buildResponse(order);
    }

    private OrderResponse buildResponse(Order order) {

        List<OrderItemResponse> items =
                orderItemRepository.findByOrderId(order.getId())
                        .stream()
                        .map(OrderItemResponse::from)
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                items
        );
    }
}
