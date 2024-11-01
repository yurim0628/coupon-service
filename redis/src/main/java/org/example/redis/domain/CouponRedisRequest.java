package org.example.redis.domain;

public record CouponRedisRequest(
        Long couponId,
        Long maxQuantity,
        Long eventId,
        String userId
) {
}
