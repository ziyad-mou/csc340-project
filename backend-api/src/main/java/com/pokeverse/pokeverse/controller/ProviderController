package com.pokeverse.pokeverse.controller;

import java.util.List;

import com.pokeverse.pokeverse.dto.*;
import com.pokeverse.pokeverse.entity.CardListing;
import com.pokeverse.pokeverse.entity.Provider;
import com.pokeverse.pokeverse.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService service;

    public ProviderController(ProviderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Provider create(
            @Valid @RequestBody ProviderCreateRequest request) {

        return service.create(request);
    }

    @GetMapping("/{providerId}")
    public Provider get(@PathVariable Long providerId) {
        return service.get(providerId);
    }

    @PutMapping("/{providerId}")
    public Provider update(
            @PathVariable Long providerId,
            @Valid @RequestBody ProviderUpdateRequest request) {

        return service.update(providerId, request);
    }

    @DeleteMapping("/{providerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long providerId) {
        service.delete(providerId);
    }

    @PostMapping("/{providerId}/cards")
    @ResponseStatus(HttpStatus.CREATED)
    public CardListing createCard(
            @PathVariable Long providerId,
            @Valid @RequestBody CardListingRequest request) {

        return service.createCard(providerId, request);
    }

    @GetMapping("/{providerId}/cards")
    public List<CardListing> listCards(
            @PathVariable Long providerId) {

        return service.listCards(providerId);
    }

    @GetMapping("/{providerId}/cards/{cardId}")
    public CardListing getCard(
            @PathVariable Long providerId,
            @PathVariable Long cardId) {

        return service.getCard(providerId, cardId);
    }

    @PutMapping("/{providerId}/cards/{cardId}")
    public CardListing updateCard(
            @PathVariable Long providerId,
            @PathVariable Long cardId,
            @Valid @RequestBody CardListingRequest request) {

        return service.updateCard(providerId, cardId, request);
    }

    @PutMapping("/{providerId}/cards/{cardId}/inventory")
    public CardListing updateStock(
            @PathVariable Long providerId,
            @PathVariable Long cardId,
            @Valid @RequestBody InventoryUpdateRequest request) {

        return service.updateStock(providerId, cardId, request.stock());
    }

    @DeleteMapping("/{providerId}/cards/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(
            @PathVariable Long providerId,
            @PathVariable Long cardId) {

        service.deleteCard(providerId, cardId);
    }

    @GetMapping("/{providerId}/statistics")
    public SalesStatistics statistics(
            @PathVariable Long providerId) {

        return service.statistics(providerId);
    }

    @GetMapping("/{providerId}/reviews")
    public List<ReviewResponse> reviews(
            @PathVariable Long providerId) {

        return service.listReviews(providerId);
    }

    @PostMapping("/{providerId}/reviews/{reviewId}/replies")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse reply(
            @PathVariable Long providerId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewReplyRequest request) {

        return service.reply(providerId, reviewId, request, true);
    }

    @PutMapping("/{providerId}/reviews/{reviewId}/replies")
    public ReviewResponse updateReply(
            @PathVariable Long providerId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewReplyRequest request) {

        return service.reply(providerId, reviewId, request, false);
    }
}