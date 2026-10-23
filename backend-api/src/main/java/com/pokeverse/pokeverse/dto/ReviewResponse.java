
package com.pokeverse.pokeverse.dto;

import java.time.LocalDateTime;

import com.pokeverse.pokeverse.entity.Review;

public record ReviewResponse(
        Long id,
        Long customerId,
        Long cardListingId,
        Integer rating,
        String comment,
        String sellerReply,
        LocalDateTime createdAt,
        LocalDateTime repliedAt
) {

    public static ReviewResponse from(Review review) {

        return new ReviewResponse(
                review.getId(),
                review.getCustomer().getId(),
                review.getCardListing().getId(),
                review.getRating(),
                review.getComment(),
                review.getSellerReply(),
                review.getCreatedAt(),
                review.getRepliedAt()
        );
    }
}
