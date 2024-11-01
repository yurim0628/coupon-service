package org.example.issue_coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.issue_coupon.domain.Coupon;
import org.example.issue_coupon.domain.CouponRedis;
import org.example.issue_coupon.domain.CouponRedisRequest;
import org.example.issue_coupon.service.port.RestApiClient;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestApiService {

    private final RestApiClient restApiClient;

    public Optional<CouponRedis> getCouponFromRedis(String couponRedisUri, Class<CouponRedis> couponRedisClass) {
        return restApiClient.get(couponRedisUri, couponRedisClass);
    }

    public Optional<Coupon> getCouponFromDb(String couponDbUri, Class<Coupon> couponClass) {
        return restApiClient.get(couponDbUri, couponClass);
    }

    public void saveCouponToRedis(String postRedisCouponUrl, CouponRedis couponRedis, Class<CouponRedis> couponReidsClass) {
        restApiClient.post(postRedisCouponUrl, couponRedis, couponReidsClass);
    }

    public void validateCouponIssueInRedis(String validationUri, CouponRedisRequest couponRedisRequest, Class<CouponRedisRequest> couponRedisRequestClass) {
        restApiClient.post(validationUri, couponRedisRequest, couponRedisRequestClass);
    }
}
