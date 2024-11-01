package org.example.redis.controller;

import lombok.RequiredArgsConstructor;
import org.example.redis.domain.CouponRedisRequest;
import org.example.redis.service.CouponIssueRedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis/coupons-issues")
@RequiredArgsConstructor
public class CouponIssueRedisController {

    private final CouponIssueRedisService couponIssueRedisService;

    @PostMapping("/validate")
    public ResponseEntity<Void> validateCouponIssue(@RequestBody CouponRedisRequest couponRedisRequest) {
        couponIssueRedisService.checkCouponIssueQuantityAndDuplicate(couponRedisRequest);
        return ResponseEntity.ok().build();
    }
}
