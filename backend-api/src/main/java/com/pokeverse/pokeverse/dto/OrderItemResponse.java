
package com.pokeverse.pokeverse.dto;

import java.math.BigDecimal;

import com.pokeverse.pokeverse.entity.OrderItem;

public record OrderItemResponse(
        Long id,
        Long cardListingId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {

    public static OrderItemResponse from(OrderItem item) {

        BigDecimal subtotal = item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemResponse(
                item.getId(),
                item.getCardListing().getId(),
                item.getQuantity(),
                item.getUnitPrice(),
                subtotal
        );
    }
}
