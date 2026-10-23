
package com.pokeverse.pokeverse.dto;

import com.pokeverse.pokeverse.entity.Customer;

public record CustomerResponse(
    Long id,
    String name,
    String email,
    String shippingAddress,
    String accountStatus
) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getShippingAddress(),
            customer.getAccountStatus()
        );
    }
}
