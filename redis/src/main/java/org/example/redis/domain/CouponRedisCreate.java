package org.example.redis.domain;

public record CouponRedisCreate(
        Long id,
        Long maxQuantity,
        Long eventId
) {
}
