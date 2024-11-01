package org.example.redis.domain;

public record CouponRedis(
        Long id,
        Long maxQuantity,
        Long eventId
) {
    public static CouponRedis of(CouponRedisCreate couponRedisCreate) {
        return new CouponRedis(
                couponRedisCreate.id(),
                couponRedisCreate.maxQuantity(),
                couponRedisCreate.eventId()
        );
    }
}
