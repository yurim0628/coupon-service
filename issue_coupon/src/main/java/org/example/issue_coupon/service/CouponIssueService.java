package org.example.issue_coupon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.exception.CommonException;
import org.example.issue_coupon.domain.Coupon;
import org.example.issue_coupon.domain.CouponIssueCreate;
import org.example.issue_coupon.domain.CouponRedis;
import org.example.issue_coupon.domain.CouponRedisRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.example.common.exception.ErrorCode.COUPON_NOT_FOUND;
import static org.example.issue_coupon.utils.RequestUrlUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final RestApiService restApiService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void issueCoupon(CouponIssueCreate couponIssueRequest) {
        Long couponId = couponIssueRequest.couponId();
        String userId = String.valueOf(couponIssueRequest.userId());
        log.info("Issuing Coupon. " +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);

        validateCouponIssue(getCachedCoupon(couponId), userId);
        kafkaTemplate.send("topic", couponIssueRequest);
        log.info("Coupon Issued Successfully. " +
                "Coupon ID: [{}], User ID: [{}]", couponId, userId);
    }

    private CouponRedis getCachedCoupon(Long couponId) {
        return restApiService.getCouponFromRedis(
                buildUriWithPathVariable(REDIS_COUPONS_ENDPOINT, couponId),
                CouponRedis.class
        ).orElseGet(() -> fetchCouponFromDbAndCache(couponId));
    }

    private CouponRedis fetchCouponFromDbAndCache(Long couponId) {
        log.info("Cache Miss for Coupon. Fetching from DB. " +
                "Coupon ID: [{}]", couponId);

        Coupon couponFromDb = restApiService.getCouponFromDb(
                buildUriWithPathVariable(EVENTS_COUPONS_ENDPOINT, couponId),
                Coupon.class
        ).orElseThrow(() -> new CommonException(COUPON_NOT_FOUND));

        CouponRedis couponRedis = CouponRedis.from(couponFromDb);
        cacheCoupon(couponRedis);

        return couponRedis;
    }

    private void cacheCoupon(CouponRedis couponRedis) {
        restApiService.saveCouponToRedis(
                buildUri(REDIS_COUPONS_ENDPOINT),
                couponRedis,
                CouponRedis.class
        );
    }

    private void validateCouponIssue(CouponRedis cachedCoupon, String userId) {
        restApiService.validateCouponIssueInRedis(
                buildUri(REDIS_COUPONS_ISSUES_VALIDATE_ENDPOINT),
                CouponRedisRequest.from(cachedCoupon, userId),
                CouponRedisRequest.class
        );
    }
}
