
package com.pokeverse.pokeverse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokeverse.pokeverse.dto.CreateReviewRequest;
import com.pokeverse.pokeverse.dto.ReviewResponse;
import com.pokeverse.pokeverse.dto.UpdateReviewRequest;
import com.pokeverse.pokeverse.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // =====================================
    // CREATE A REVIEW
    // =====================================

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request) {

        ReviewResponse response =
                reviewService.createReview(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =====================================
    // UPDATE A REVIEW
    // =====================================

    @PutMapping("/{reviewId}")
    public ReviewResponse updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequest request) {

        return reviewService.updateReview(reviewId, request);
    }

    // =====================================
    // GET ONE REVIEW
    // =====================================

    @GetMapping("/{reviewId}")
    public ReviewResponse getReview(
            @PathVariable Long reviewId) {

        return reviewService.getReview(reviewId);
    }

    // =====================================
    // GET REVIEWS FOR A CARD
    // =====================================

    @GetMapping("/card/{cardListingId}")
    public List<ReviewResponse> getCardReviews(
            @PathVariable Long cardListingId) {

        return reviewService.getCardReviews(cardListingId);
    }

    // =====================================
    // GET REVIEWS BY A CUSTOMER
    // =====================================

    @GetMapping("/customer/{customerId}")
    public List<ReviewResponse> getCustomerReviews(
            @PathVariable Long customerId) {

        return reviewService.getCustomerReviews(customerId);
    }
}
