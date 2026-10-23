
package com.pokeverse.pokeverse.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.pokeverse.pokeverse.dto.CreateReviewRequest;
import com.pokeverse.pokeverse.dto.ReviewResponse;
import com.pokeverse.pokeverse.dto.UpdateReviewRequest;
import com.pokeverse.pokeverse.entity.CardListing;
import com.pokeverse.pokeverse.entity.Customer;
import com.pokeverse.pokeverse.entity.Review;
import com.pokeverse.pokeverse.repository.CardListingRepository;
import com.pokeverse.pokeverse.repository.CustomerRepository;
import com.pokeverse.pokeverse.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final CardListingRepository cardListingRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            CustomerRepository customerRepository,
            CardListingRepository cardListingRepository) {

        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.cardListingRepository = cardListingRepository;
    }

    // =====================================
    // CREATE A REVIEW
    // =====================================

    @Transactional
    public ReviewResponse createReview(
            CreateReviewRequest request) {

        Customer customer = customerRepository
                .findById(request.customerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found"
                ));

        CardListing card = cardListingRepository
                .findById(request.cardListingId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card listing not found"
                ));

        Review review = new Review();

        review.setCustomer(customer);
        review.setCardListing(card);
        review.setRating(request.rating());
        review.setComment(request.comment());

        Review savedReview = reviewRepository.save(review);

        return ReviewResponse.from(savedReview);
    }

    // =====================================
    // UPDATE A REVIEW
    // =====================================

    @Transactional
    public ReviewResponse updateReview(
            Long reviewId,
            UpdateReviewRequest request) {

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review not found"
                ));

        if (!review.getCustomer().getId()
                .equals(request.customerId())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Review belongs to another customer"
            );
        }

        review.setRating(request.rating());
        review.setComment(request.comment());

        Review updatedReview = reviewRepository.save(review);

        return ReviewResponse.from(updatedReview);
    }

    // =====================================
    // GET ONE REVIEW
    // =====================================

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long reviewId) {

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review not found"
                ));

        return ReviewResponse.from(review);
    }

    // =====================================
    // GET REVIEWS FOR A CARD
    // =====================================

    @Transactional(readOnly = true)
    public List<ReviewResponse> getCardReviews(
            Long cardListingId) {

        if (!cardListingRepository.existsById(cardListingId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Card listing not found"
            );
        }

        return reviewRepository
                .findByCardListingIdOrderByCreatedAtDesc(cardListingId)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    // =====================================
    // GET REVIEWS BY A CUSTOMER
    // =====================================

    @Transactional(readOnly = true)
    public List<ReviewResponse> getCustomerReviews(
            Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found"
            );
        }

        return reviewRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }
}
