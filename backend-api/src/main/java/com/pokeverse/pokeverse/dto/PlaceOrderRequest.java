
package com.pokeverse.pokeverse.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlaceOrderRequest(

        @NotNull
        @Positive
        Long customerId,

        @NotEmpty
        List<@Valid Item> items

) {
    public record Item(

            @NotNull
            @Positive
            Long cardListingId,

            @NotNull
            @Min(1)
            Integer quantity

    ) {
    }
}
