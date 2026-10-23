package com.pokeverse.pokeverse.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import com.pokeverse.pokeverse.dto.*;
import com.pokeverse.pokeverse.entity.*;
import com.pokeverse.pokeverse.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProviderService {

    private final ProviderRepository providers;
    private final CardListingRepository cards;
    private final ReviewRepository reviews;
    private final OrderItemRepository items;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public ProviderService(
            ProviderRepository providers,
            CardListingRepository cards,
            ReviewRepository reviews,
            OrderItemRepository items) {

        this.providers = providers;
        this.cards = cards;
        this.reviews = reviews;
        this.items = items;
    }

    public Provider create(ProviderCreateRequest request) {
        if (request.password()
                .getBytes(StandardCharsets.UTF_8).length > 72) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password exceeds 72 bytes"
            );
        }

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (providers.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Provider email already exists"
            );
        }

        Provider provider = new Provider();
        provider.setStoreName(request.storeName().trim());
        provider.setEmail(email);
        provider.setPasswordHash(encoder.encode(request.password()));
        provider.setBiography(request.biography());

        return saveProvider(provider);
    }

    @Transactional(readOnly = true)
    public Provider get(Long id) {
        return requireProvider(id);
    }

    public Provider update(Long id, ProviderUpdateRequest request) {
        Provider provider = lockProvider(id);

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (providers.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Provider email already exists"
            );
        }

        provider.setStoreName(request.storeName().trim());
        provider.setEmail(email);
        provider.setBiography(request.biography());

        return saveProvider(provider);
    }

    public void delete(Long id) {
        Provider provider = lockProvider(id);

        if (cards.existsByProviderId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Provider still owns listings"
            );
        }

        providers.delete(provider);
    }

    public CardListing createCard(
            Long providerId,
            CardListingRequest request) {

        Provider provider = lockProvider(providerId);

        CardListing card = new CardListing();
        card.setProviderId(provider.getId());
        card.setProvider(provider);
        copyCard(request, card);

        return cards.saveAndFlush(card);
    }

    @Transactional(readOnly = true)
    public List<CardListing> listCards(Long providerId) {
        requireProvider(providerId);
        return cards.findByProviderIdOrderByIdDesc(providerId);
    }

    @Transactional(readOnly = true)
    public CardListing getCard(Long providerId, Long cardId) {
        return ownedCard(providerId, cardId, false);
    }

    public CardListing updateCard(
            Long providerId,
            Long cardId,
            CardListingRequest request) {

        CardListing card = ownedCard(providerId, cardId, true);
        copyCard(request, card);

        return cards.saveAndFlush(card);
    }

    public CardListing updateStock(
            Long providerId,
            Long cardId,
            Integer stock) {

        CardListing card = ownedCard(providerId, cardId, true);
        card.setStock(stock);

        return cards.saveAndFlush(card);
    }

    public void deleteCard(Long providerId, Long cardId) {
        CardListing card = ownedCard(providerId, cardId, true);

        if (items.existsByCardListingId(cardId)
                || reviews.existsByCardListingId(cardId)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Listing has orders or reviews"
            );
        }

        try {
            cards.delete(card);
            cards.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Listing has orders or reviews"
            );
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> listReviews(Long providerId) {
        requireProvider(providerId);

        return reviews
                .findByCardListing_ProviderIdOrderByCreatedAtDesc(providerId)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    public ReviewResponse reply(
            Long providerId,
            Long reviewId,
            ReviewReplyRequest request,
            boolean create) {

        requireProvider(providerId);

        Review review = reviews.findByIdForUpdate(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review not found"
                ));

        if (!Objects.equals(
                review.getCardListing().getProviderId(),
                providerId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Review not found for this provider"
            );
        }

        if (create && review.getSellerReply() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Reply already exists; use PUT"
            );
        }

        if (!create && review.getSellerReply() == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Create the reply with POST first"
            );
        }

        review.setSellerReply(request.sellerReply().trim());
        review.setRepliedAt(LocalDateTime.now());

        return ReviewResponse.from(reviews.saveAndFlush(review));
    }

    @Transactional(readOnly = true)
    public SalesStatistics statistics(Long providerId) {
        requireProvider(providerId);

        Set<Long> orderIds = new HashSet<>();
        Set<Long> customerIds = new HashSet<>();

        BigDecimal revenue = new BigDecimal("0.00");
        long sold = 0;

        for (OrderItem item : items.findSalesByProviderId(providerId)) {
            orderIds.add(item.getOrder().getId());
            customerIds.add(item.getOrder().getCustomer().getId());

            sold += item.getQuantity();

            revenue = revenue.add(
                    item.getUnitPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    )
            );
        }

        return new SalesStatistics(
                orderIds.size(),
                revenue,
                sold,
                customerIds.size()
        );
    }

    private Provider requireProvider(Long id) {
        return providers.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Provider not found"
                ));
    }

    private Provider lockProvider(Long id) {
        return providers.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Provider not found"
                ));
    }

    private Provider saveProvider(Provider provider) {
        try {
            return providers.saveAndFlush(provider);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Provider email already exists"
            );
        }
    }

    private CardListing ownedCard(
            Long providerId,
            Long cardId,
            boolean lock) {

        requireProvider(providerId);

        CardListing card = (
                lock
                        ? cards.findByIdForUpdate(cardId)
                        : cards.findById(cardId)
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Listing not found"
        ));

        if (!Objects.equals(card.getProviderId(), providerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Listing not found for this provider"
            );
        }

        return card;
    }

    private void copyCard(
            CardListingRequest request,
            CardListing card) {

        card.setName(request.name().trim());
        card.setDescription(request.description().trim());
        card.setCondition(request.condition().trim());
        card.setPrice(request.price());
        card.setStock(request.stock());
        card.setImageUrl(request.imageUrl());
    }
}