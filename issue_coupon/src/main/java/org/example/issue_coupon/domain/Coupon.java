package org.example.issue_coupon.domain;

import org.example.coupon.domain.DiscountType;

import java.time.LocalDateTime;

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
