package org.example.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.response.Response;
import org.example.coupon.domain.CouponResponse;
import org.example.coupon.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventCouponController {

    private final CouponService couponService;

    @GetMapping("/{eventId}/coupons")
    public ResponseEntity<Response<List<CouponResponse>>> getCoupons(@PathVariable Long eventId) {
        return ResponseEntity.ok(Response.success(couponService.getCoupons(eventId)));
    }
}
