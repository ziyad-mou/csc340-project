package com.pokeverse.pokeverse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pokeverse.pokeverse.entity.Customer;
import com.pokeverse.pokeverse.service.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class CustomerLoginController {

    private final CustomerService customerService;

    public CustomerLoginController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Display the login page
    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null
                && session.getAttribute("customerId") != null) {
            return "redirect:/customer/cart";
        }

        return "customer/login";
    }

    // Process customer login
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        try {

            Customer customer =
                    customerService.authenticateCustomer(
                            email, password
                    );

            // Replace the old session after login
            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Create a new authenticated session
            HttpSession session = request.getSession(true);

            session.setAttribute(
                    "customerId",
                    customer.getId()
            );

            return "redirect:/customer/cart";

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Invalid email or password."
            );

            return "redirect:/customer/login";
        }
    }

    // Log the customer out
    @PostMapping("/logout")
    public String logout(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "You have logged out."
        );

        return "redirect:/customer/login";
    }
}
