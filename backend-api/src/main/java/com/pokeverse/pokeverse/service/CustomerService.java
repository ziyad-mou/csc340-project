
package com.pokeverse.pokeverse.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.pokeverse.pokeverse.entity.Customer;
import com.pokeverse.pokeverse.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Create customer
    @Transactional
    public Customer createCustomer(
            String name,
            String email,
            String password,
            String shippingAddress) {

        if (customerRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already exists");
        }

        Customer customer = new Customer();

        customer.setName(name);
        customer.setEmail(email);
        customer.setPasswordHash(passwordEncoder.encode(password));
        customer.setShippingAddress(shippingAddress);

        return customerRepository.save(customer);
    }

    // Verify customer login
    @Transactional(readOnly = true)
    public Customer authenticateCustomer(
            String email,
            String password) {

        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }

        Customer customer = customerRepository
                .findByEmail(email.trim())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid email or password."
                ));

        if (!passwordEncoder.matches(
                password,
                customer.getPasswordHash())) {

            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }

        return customer;
    }

    // Get customer by ID
    @Transactional(readOnly = true)
    public Customer getCustomer(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Customer not found"));
    }

    // Update customer profile
    @Transactional
    public Customer updateCustomer(
            Long id,
            String name,
            String email,
            String shippingAddress) {

        Customer customer = getCustomer(id);

        if (!customer.getEmail().equals(email)
                && customerRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already exists");
        }

        customer.setName(name);
        customer.setEmail(email);
        customer.setShippingAddress(shippingAddress);

        return customerRepository.save(customer);
    }
}
