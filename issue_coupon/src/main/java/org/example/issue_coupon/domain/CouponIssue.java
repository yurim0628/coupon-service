package org.example.issue_coupon.domain;

import lombok.Builder;
import lombok.Getter;

import static org.example.issue_coupon.domain.CouponStatus.ACTIVE;

@Getter
public class CouponIssue {
    private final Long id;
    private final CouponStatus couponStatus;
    private final Long couponId;

    @Builder
    private CouponIssue(Long id, CouponStatus couponStatus, Long couponId) {
        this.id = id;
        this.couponStatus = couponStatus;
        this.couponId = couponId;
    }

    public static CouponIssue from(CouponIssueCreate couponIssueCreate) {
        return CouponIssue.builder()
                .couponStatus(ACTIVE)
                .couponId(couponIssueCreate.couponId())
                .build();
    }
}
