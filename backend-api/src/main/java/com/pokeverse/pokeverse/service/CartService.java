
package com.pokeverse.pokeverse.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pokeverse.pokeverse.entity.CardListing;
import com.pokeverse.pokeverse.entity.CartItem;
import com.pokeverse.pokeverse.entity.Customer;
import com.pokeverse.pokeverse.repository.CardListingRepository;
import com.pokeverse.pokeverse.repository.CartItemRepository;
import com.pokeverse.pokeverse.repository.CustomerRepository;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CardListingRepository cardListingRepository;
    private final CustomerRepository customerRepository;

    public CartService(
            CartItemRepository cartItemRepository,
            CardListingRepository cardListingRepository,
            CustomerRepository customerRepository) {

        this.cartItemRepository = cartItemRepository;
        this.cardListingRepository = cardListingRepository;
        this.customerRepository = customerRepository;
    }

    // Add a card to a customer's cart
    @Transactional
    public CartItem addToCart(
            Long customerId,
            Long cardListingId,
            int quantity) {

        if (quantity < 1) {
            throw new IllegalArgumentException(
                    "Quantity must be at least 1."
            );
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Customer not found."
                ));

        CardListing card = cardListingRepository.findById(cardListingId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Card listing not found."
                ));

        CartItem cartItem = cartItemRepository
                .findByCustomerIdAndCardListingId(
                        customerId, cardListingId
                )
                .orElse(null);

        long newQuantity = (long) quantity;

        if (cartItem != null) {
            newQuantity += cartItem.getQuantity();
        }

        validateStock(card, newQuantity);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setCardListing(card);
        }

        cartItem.setQuantity((int) newQuantity);

        return cartItemRepository.save(cartItem);
    }

    // Retrieve a customer's cart items
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long customerId) {

        return cartItemRepository
                .findByCustomerIdOrderByIdAsc(customerId);
    }

    // Update the quantity of a cart item
    @Transactional
    public CartItem updateQuantity(
            Long customerId,
            Long cartItemId,
            int quantity) {

        if (quantity < 1) {
            throw new IllegalArgumentException(
                    "Quantity must be at least 1."
            );
        }

        CartItem cartItem = findOwnedCartItem(
                customerId, cartItemId
        );

        validateStock(cartItem.getCardListing(), quantity);

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    // Remove a card from the cart
    @Transactional
    public void removeItem(
            Long customerId,
            Long cartItemId) {

        CartItem cartItem = findOwnedCartItem(
                customerId, cartItemId
        );

        cartItemRepository.delete(cartItem);
    }

    // Make sure the item belongs to the customer
    private CartItem findOwnedCartItem(
            Long customerId,
            Long cartItemId) {

        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cart item not found."
                ));

        if (!cartItem.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException(
                    "Cart item does not belong to this customer."
            );
        }

        return cartItem;
    }

    // Prevent customers from requesting too many cards
    private void validateStock(
            CardListing card,
            long quantity) {

        Integer stock = card.getStock();

        if (stock == null || quantity > stock) {
            throw new IllegalArgumentException(
                    "Not enough cards in stock."
            );
        }
    }
}
