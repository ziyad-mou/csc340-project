
package com.pokeverse.pokeverse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokeverse.pokeverse.dto.CustomerCreateRequest;
import com.pokeverse.pokeverse.dto.CustomerResponse;
import com.pokeverse.pokeverse.dto.CustomerUpdateRequest;
import com.pokeverse.pokeverse.entity.Customer;
import com.pokeverse.pokeverse.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create Customer
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerCreateRequest request) {

        Customer customer = customerService.createCustomer(
                request.name(),
                request.email(),
                request.password(),
                request.shippingAddress()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomerResponse.from(customer));
    }

    // Get Customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(
            @PathVariable Long id) {

        Customer customer = customerService.getCustomer(id);

        return ResponseEntity.ok(
                CustomerResponse.from(customer)
        );
    }

    // Update Customer
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request) {

        Customer customer = customerService.updateCustomer(
                id,
                request.name(),
                request.email(),
                request.shippingAddress()
        );

        return ResponseEntity.ok(
                CustomerResponse.from(customer)
        );
    }
}