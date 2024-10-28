package org.example.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.redis.domain.CouponRedis;
import org.example.redis.service.port.CouponCache;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.redis.utils.RedisKeyUtils.getCouponKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRedisService {

    private final CouponCache couponCache;

    public Optional<CouponRedis> getCoupon(Long couponId) {
        String couponKey = getCouponKey(couponId);
        log.info("Getting coupon with Coupon ID: [{}], Generated Key: [{}]", couponId, couponKey);
        return couponCache.getCoupon(couponKey);
    }

    public void setCoupon(Long couponId, CouponRedis couponRedis) {
        String couponKey = getCouponKey(couponId);
        log.info("Setting Coupon with Coupon ID: [{}], Key: [{}]", couponId, couponKey);
        couponCache.setCoupon(couponKey, couponRedis);
    }
}
