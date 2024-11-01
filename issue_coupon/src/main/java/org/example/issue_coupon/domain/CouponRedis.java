package org.example.issue_coupon.domain;

public record CouponRedis(
        Long id,
        Long maxQuantity,
        Long eventId
) {
    public static CouponRedis from(Coupon coupon) {
        return new CouponRedis(
                coupon.id(),
                coupon.maxQuantity(),
                coupon.eventId()
        );
    }
}
