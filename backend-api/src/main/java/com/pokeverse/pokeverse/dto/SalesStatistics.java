package com.pokeverse.pokeverse.dto;

import java.math.BigDecimal;

public record SalesStatistics(
        long totalSales,
        BigDecimal totalRevenue,
        long cardsSold,
        long uniqueCustomers
) {
}