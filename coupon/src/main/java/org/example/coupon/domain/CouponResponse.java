package org.example.coupon.domain;

import java.time.LocalDateTime;

import static org.example.coupon.domain.DiscountType.AMOUNT;
import static org.example.coupon.domain.DiscountType.PERCENT;

public record CouponResponse(
        Long id,
        Long discountValue,
        LocalDateTime validateStartDate,
        LocalDateTime validateEndDate
) {

    public static CouponResponse of(Coupon coupon) {
        return new CouponResponse(
                coupon.id(),
                getDiscountValue(coupon),
                coupon.validateStartDate(),
                coupon.validateEndDate()
        );
    }

    private static Long getDiscountValue(Coupon coupon) {
        DiscountType discountType = coupon.discountType();
        if (discountType.equals(PERCENT)) {
            return coupon.discountRate();
        } else if (discountType.equals(AMOUNT)) {
            return coupon.discountPrice();
        }
        return null;
    }
}
