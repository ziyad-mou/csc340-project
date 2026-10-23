
package com.pokeverse.pokeverse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokeverse.pokeverse.dto.OrderResponse;
import com.pokeverse.pokeverse.dto.PlaceOrderRequest;
import com.pokeverse.pokeverse.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // =========================================
    // CREATE AN ORDER
    // =========================================

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody PlaceOrderRequest request) {

        List<OrderService.OrderLine> items =
                request.items()
                        .stream()
                        .map(item -> new OrderService.OrderLine(
                                item.cardListingId(),
                                item.quantity()
                        ))
                        .toList();

        OrderResponse response = orderService.placeOrder(
                request.customerId(),
                items
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================
    // GET CUSTOMER PURCHASE HISTORY
    // =========================================

    @GetMapping("/customer/{customerId}")
    public List<OrderResponse> getPurchaseHistory(
            @PathVariable Long customerId) {

        return orderService.getPurchaseHistory(customerId);
    }

    // =========================================
    // GET ONE ORDER
    // =========================================

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(
            @PathVariable Long orderId) {

        return orderService.getOrder(orderId);
    }
}
