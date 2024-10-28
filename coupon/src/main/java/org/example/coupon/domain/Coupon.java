package org.example.coupon.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Coupon(
        Long id,
        DiscountType discountType,
        Long discountRate,
        Long discountPrice,
        Long maxQuantity,
        Long issuedQuantity,
        LocalDateTime validateStartDate,
        LocalDateTime validateEndDate,
        Long eventId
) {
}

