package com.pokeverse.pokeverse.repository;

import java.util.List;

import com.pokeverse.pokeverse.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    boolean existsByCardListingId(Long cardListingId);

    @Query("""
            select i from OrderItem i
            join fetch i.order o
            join fetch o.customer
            join fetch i.cardListing c
            where c.providerId = :providerId and o.status <> 'CANCELLED'
            """)
    List<OrderItem> findSalesByProviderId(
            @Param("providerId") Long providerId
    );
}