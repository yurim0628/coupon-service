package org.example.coupon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.domain.Coupon;
import org.example.coupon.service.port.CouponRepository;
import org.example.redis.service.CouponIssueRedisService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponSyncScheduler {

    private static final Long NO_ISSUED_COUNT = 0L;
    private final CouponService couponService;
    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponRepository couponRepository;

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    @Transactional
    public void issuedQuantityUpdate() {
        List<Coupon> coupons = couponService.getAllCoupons();
        for (Coupon coupon : coupons) {
            Long couponId = coupon.id();
            Long issuedCount = couponIssueRedisService.getIssuedCouponCount(couponId);
            if (!Objects.equals(issuedCount, NO_ISSUED_COUNT)) {
                couponRepository.updateIssuedQuantity(couponId, issuedCount);
            }
        }
    }
}
