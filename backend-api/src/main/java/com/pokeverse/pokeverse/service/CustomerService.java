package com.pokeverse.pokeverse.service;

import com.pokeverse.pokeverse.entity.Customer;
import com.pokeverse.pokeverse.repository.CustomerRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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