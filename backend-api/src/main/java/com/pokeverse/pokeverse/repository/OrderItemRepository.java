
package com.pokeverse.pokeverse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pokeverse.pokeverse.entity.OrderItem;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

}
