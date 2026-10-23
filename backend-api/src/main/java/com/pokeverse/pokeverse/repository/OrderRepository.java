
package com.pokeverse.pokeverse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pokeverse.pokeverse.entity.Order;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId
    );
}
