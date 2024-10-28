package org.example.redis.domain;

import lombok.Builder;

@Builder
public record CouponRedis(
        Long id,
        Long maxQuantity,
        Long eventId
) {
}
