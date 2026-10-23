
package com.pokeverse.pokeverse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pokeverse.pokeverse.entity.CardListing;

public interface CardListingRepository
        extends JpaRepository<CardListing, Long> {

    List<CardListing> findByNameContainingIgnoreCase(String name);

}
