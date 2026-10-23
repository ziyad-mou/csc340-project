package com.pokeverse.pokeverse.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CardListingRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Size(max = 255) String description,
        @NotBlank @Size(max = 50) String condition,
        @NotNull @PositiveOrZero
        @Digits(integer = 10, fraction = 2) BigDecimal price,
        @NotNull @Min(0) Integer stock,
        @Size(max = 255) String imageUrl
) {
}