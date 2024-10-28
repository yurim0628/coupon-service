package org.example.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.redis.domain.CouponRedis;
import org.example.redis.infrastructure.entity.CouponRedisEntity;
import org.example.redis.service.port.CouponCache;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponCacheImpl implements CouponCache {

    private final CouponRedisCache couponRedisCache;

    @Override
    public Optional<CouponRedis> getCoupon(String key) {
        Optional<CouponRedisEntity> couponEntity = couponRedisCache.getCoupon(key);
        if (couponEntity.isPresent()) {
            log.info("Coupon Found in Cache: [{}]", key);
            return couponEntity.map(CouponRedisEntity::toModel);
        } else {
            log.info("Coupon Not Found in Cache: [{}]", key);
            return Optional.empty();
        }
    }

    @Override
    public void setCoupon(String key, CouponRedis couponRedis) {
        couponRedisCache.setCoupon(key, couponRedis);
        log.info("Coupon Set in Cache: [{}]", key);
    }
}feat: CouponCacheImpl 구현으로 Redis 캐시에서 쿠폰 데이터 조회 및 저장 기능 추가
