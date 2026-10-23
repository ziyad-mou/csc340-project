
package com.pokeverse.pokeverse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pokeverse.pokeverse.entity.CardListing;

import jakarta.persistence.LockModeType;

public interface CardListingRepository
        extends JpaRepository<CardListing, Long> {

    List<CardListing> findByNameContainingIgnoreCase(
            String name
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CardListing c WHERE c.id = :id")
    Optional<CardListing> findByIdForUpdate(
            @Param("id") Long id
    );
}
