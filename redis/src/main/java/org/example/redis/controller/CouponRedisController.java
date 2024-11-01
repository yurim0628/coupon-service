package org.example.redis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.response.Response;
import org.example.redis.domain.CouponRedis;
import org.example.redis.domain.CouponRedisCreate;
import org.example.redis.service.CouponRedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/redis/coupons")
@RequiredArgsConstructor
public class CouponRedisController {

    private final CouponRedisService couponRedisService;

    @GetMapping("/{couponId}")
    public ResponseEntity<Response<Optional<CouponRedis>>> getCoupon(@PathVariable Long couponId) {
        return ResponseEntity.ok(Response.success(couponRedisService.getCoupon(couponId)));
    }

    @PostMapping
    public ResponseEntity<Void> setCoupon(@RequestBody @Valid CouponRedisCreate couponRedisCreate) {
        couponRedisService.setCoupon(couponRedisCreate);
        return ResponseEntity.ok().build();
    }
}
