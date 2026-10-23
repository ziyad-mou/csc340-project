
package com.pokeverse.pokeverse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pokeverse.pokeverse.entity.CardListing;
import com.pokeverse.pokeverse.repository.CardListingRepository;

@Service
public class CardBrowseService {

    private final CardListingRepository cardListingRepository;

    public CardBrowseService(
            CardListingRepository cardListingRepository) {
        this.cardListingRepository = cardListingRepository;
    }

    public List<CardListing> browseCards(String search) {

        if (search == null || search.isBlank()) {
            return cardListingRepository.findAll();
        }

        return cardListingRepository
                .findByNameContainingIgnoreCase(search.trim());
    }
}
