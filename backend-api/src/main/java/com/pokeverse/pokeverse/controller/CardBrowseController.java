
package com.pokeverse.pokeverse.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pokeverse.pokeverse.entity.CardListing;
import com.pokeverse.pokeverse.service.CardBrowseService;

@RestController
@RequestMapping("/api/cards")
public class CardBrowseController {

    private final CardBrowseService cardBrowseService;

    public CardBrowseController(CardBrowseService cardBrowseService) {
        this.cardBrowseService = cardBrowseService;
    }

    @GetMapping
    public List<CardListing> browseCards(
            @RequestParam(required = false) String search) {

        return cardBrowseService.browseCards(search);
    }
}
