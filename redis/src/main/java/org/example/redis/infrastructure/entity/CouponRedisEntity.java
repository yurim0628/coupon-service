package org.example.redis.infrastructure.entity;

import org.example.redis.domain.CouponRedis;

public record CouponRedisEntity(
        Long id,
        Long maxQuantity,
        Long eventId
) {
    public static CouponRedisEntity fromModel(CouponRedis couponRedis) {
        return new CouponRedisEntity(
                couponRedis.id(),
                couponRedis.maxQuantity(),
                couponRedis.eventId()
        );
    }

    public CouponRedis toModel() {
        return CouponRedis.builder()
                .id(id)
                .maxQuantity(maxQuantity)
                .eventId(eventId)
                .build();
    }
}
