package org.example.issue_coupon.domain;

public record CouponRedisRequest(
        Long couponId,
        Long maxQuantity,
        Long eventId,
        String userId
) {
    public static CouponRedisRequest from(CouponRedis couponRedis, String userId) {
        return new CouponRedisRequest(
                couponRedis.id(),
                couponRedis.maxQuantity(),
                couponRedis.eventId(),
                userId
        );
    }
}
