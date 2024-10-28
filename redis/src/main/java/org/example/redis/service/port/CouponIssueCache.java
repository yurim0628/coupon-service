package org.example.redis.service.port;

public interface CouponIssueCache {

    Long getIssuedCouponUserCount(String issueRequestKey);

    Long addIssuedCouponUser(String key, String userId);
}
