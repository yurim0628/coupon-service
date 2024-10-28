package org.example.issue_coupon.service.port;

import org.example.issue_coupon.domain.CouponIssue;

public interface CouponIssueRepository {

    CouponIssue save(CouponIssue couponIssue);
}
