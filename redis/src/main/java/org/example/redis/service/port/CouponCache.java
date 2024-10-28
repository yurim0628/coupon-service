package org.example.redis.service.port;

import org.example.redis.domain.CouponRedis;

import java.util.Optional;

public interface CouponCache {

    Optional<CouponRedis> getCoupon(String key);

    void setCoupon(String key, CouponRedis couponRedis);
}
