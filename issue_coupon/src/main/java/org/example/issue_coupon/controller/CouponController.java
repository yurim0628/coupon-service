package org.example.issue_coupon.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.issue_coupon.domain.CouponIssueCreate;
import org.example.issue_coupon.service.CouponIssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponIssueService couponIssueService;

    @PostMapping("/issues")
    public ResponseEntity<Void> asyncIssue(@RequestBody @Valid CouponIssueCreate couponIssueCreate) {
        couponIssueService.issueCoupon(couponIssueCreate);
        return ResponseEntity.ok().build();
    }
}
