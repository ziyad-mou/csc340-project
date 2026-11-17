
package com.pokeverse.pokeverse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pokeverse.pokeverse.entity.CartItem;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    @EntityGraph(attributePaths = {"cardListing"})
    List<CartItem> findByCustomerIdOrderByIdAsc(
            Long customerId
    );

    Optional<CartItem> findByCustomerIdAndCardListingId(
            Long customerId,
            Long cardListingId
    );
}
