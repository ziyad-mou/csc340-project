package com.pokeverse.pokeverse.repository;

import java.util.List;
import java.util.Optional;

import com.pokeverse.pokeverse.entity.Review;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCardListingIdOrderByCreatedAtDesc(
            Long cardListingId
    );

    List<Review> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId
    );

    List<Review> findByCardListing_ProviderIdOrderByCreatedAtDesc(
            Long providerId
    );

    boolean existsByCardListingId(Long cardListingId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Review r where r.id = :id")
    Optional<Review> findByIdForUpdate(@Param("id") Long id);
}