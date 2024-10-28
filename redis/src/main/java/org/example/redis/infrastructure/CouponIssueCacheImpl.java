package org.example.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.redis.service.port.CouponIssueCache;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueCacheImpl implements CouponIssueCache {

    private final CouponIssueRedisCache couponIssueRedisCache;

    @Override
    public Long getIssuedCouponUserCount(String issueRequestKey) {
        return couponIssueRedisCache.getIssuedCouponUserCount(issueRequestKey);
    }

    @Override
    public Long addIssuedCouponUser(String issueRequestKey, String userId) {
        return couponIssueRedisCache.addIssuedCouponUser(issueRequestKey, userId);
    }
}
