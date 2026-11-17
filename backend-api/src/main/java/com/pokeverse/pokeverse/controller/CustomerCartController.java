
package com.pokeverse.pokeverse.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pokeverse.pokeverse.entity.CartItem;
import com.pokeverse.pokeverse.service.CartService;

@Controller
@RequestMapping("/customer/cart")
public class CustomerCartController {

    private final CartService cartService;

    public CustomerCartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Display the customer's shopping cart
    @GetMapping
    public String showCart(
            @SessionAttribute(
                name = "customerId",
                required = false
            ) Long customerId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (customerId == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please log in to view your cart."
            );
            return "redirect:/customer/search";
        }

        List<CartItem> cartItems =
                cartService.getCartItems(customerId);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            BigDecimal price = item.getCardListing().getPrice();

            subtotal = subtotal.add(
                    price.multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    )
            );
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);

        return "customer/cart";
    }

    // Add a Pokémon card to the cart
    @PostMapping("/add")
    public String addToCart(
            @SessionAttribute(
                name = "customerId",
                required = false
            ) Long customerId,
            @RequestParam Long cardListingId,
            @RequestParam(defaultValue = "1") int quantity,
            RedirectAttributes redirectAttributes) {

        if (customerId == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please log in before adding cards."
            );
            return "redirect:/customer/search";
        }

        try {
            cartService.addToCart(
                    customerId, cardListingId, quantity
            );

            redirectAttributes.addFlashAttribute(
                    "success", "Card added to cart!"
            );

            return "redirect:/customer/cart";

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "error", exception.getMessage()
            );

            return "redirect:/customer/search";
        }
    }

    // Update the quantity of a cart item
    @PostMapping("/update")
    public String updateQuantity(
            @SessionAttribute(
                name = "customerId",
                required = false
            ) Long customerId,
            @RequestParam Long cartItemId,
            @RequestParam int quantity,
            RedirectAttributes redirectAttributes) {

        if (customerId == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please log in first."
            );
            return "redirect:/customer/search";
        }

        try {
            cartService.updateQuantity(
                    customerId, cartItemId, quantity
            );

            redirectAttributes.addFlashAttribute(
                    "success", "Cart updated!"
            );

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "error", exception.getMessage()
            );
        }

        return "redirect:/customer/cart";
    }

    // Remove a Pokémon card from the cart
    @PostMapping("/remove")
    public String removeItem(
            @SessionAttribute(
                name = "customerId",
                required = false
            ) Long customerId,
            @RequestParam Long cartItemId,
            RedirectAttributes redirectAttributes) {

        if (customerId == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please log in first."
            );
            return "redirect:/customer/search";
        }

        try {
            cartService.removeItem(customerId, cartItemId);

            redirectAttributes.addFlashAttribute(
                    "success", "Card removed from cart."
            );

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "error", exception.getMessage()
            );
        }

        return "redirect:/customer/cart";
    }
}
