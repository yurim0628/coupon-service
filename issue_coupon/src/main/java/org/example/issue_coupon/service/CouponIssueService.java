package org.example.issue_coupon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.domain.Coupon;
import org.example.coupon.service.CouponService;
import org.example.issue_coupon.domain.CouponIssue;
import org.example.issue_coupon.domain.CouponIssueCreate;
import org.example.issue_coupon.service.port.CouponIssueRepository;
import org.example.redis.domain.CouponRedis;
import org.example.redis.service.CouponIssueRedisService;
import org.example.redis.service.CouponRedisService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponService couponService;
    private final CouponRedisService couponCacheService;
    private final CouponIssueRedisService couponIssueCacheService;
    private final CouponIssueRepository couponIssueRepository;

    public void issueCoupon(CouponIssueCreate couponIssueRequest) {
        Long couponId = couponIssueRequest.couponId();
        String userId = String.valueOf(couponIssueRequest.userId());

        log.info("Issuing Coupon. Coupon ID: [{}], User ID: [{}]", couponId, userId);

        CouponRedis cachedCoupon = getCachedOrDbCoupon(couponId);
        validateCouponIssue(cachedCoupon, userId);
        saveCouponIssue(couponIssueRequest);

        log.info("Coupon Issued Successfully. Coupon ID: [{}], User ID: [{}]", couponId, userId);
    }

    private CouponRedis getCachedOrDbCoupon(Long couponId) {
        return couponCacheService.getCoupon(couponId)
                .orElseGet(() -> {
                    log.info("Coupon Not Found in Cache. Fetching from Database. Coupon ID: [{}]", couponId);
                    Coupon couponFromDb = couponService.getCoupon(couponId);
                    CouponRedis couponRedis = CouponRedis.builder()
                            .id(couponFromDb.id())
                            .maxQuantity(couponFromDb.maxQuantity())
                            .eventId(couponFromDb.eventId())
                            .build();
                    couponCacheService.setCoupon(couponId, couponRedis);
                    return couponRedis;
                });
    }

    private void validateCouponIssue(CouponRedis cachedCoupon, String userId) {
        couponIssueCacheService.checkCouponIssueQuantityAndDuplicate(cachedCoupon, userId);
    }

    private void saveCouponIssue(CouponIssueCreate couponIssueRequest) {
        CouponIssue couponIssue = CouponIssue.from(couponIssueRequest);
        couponIssueRepository.save(couponIssue);
    }
}
