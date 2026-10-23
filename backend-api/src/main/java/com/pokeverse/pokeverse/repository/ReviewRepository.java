
package com.pokeverse.pokeverse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pokeverse.pokeverse.entity.Review;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    // Find all reviews for a Pokemon card
    List<Review> findByCardListingIdOrderByCreatedAtDesc(
            Long cardListingId
    );

    // Find all reviews written by a customer
    List<Review> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId
    );
}
