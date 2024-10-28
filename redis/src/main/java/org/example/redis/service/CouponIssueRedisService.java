package org.example.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.exception.CommonException;
import org.example.redis.domain.CouponRedis;
import org.example.redis.service.port.CouponIssueCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import static org.example.common.exception.ErrorCode.COUPON_ALREADY_ISSUED_BY_USER;
import static org.example.common.exception.ErrorCode.COUPON_ISSUE_QUANTITY_EXCEEDED;
import static org.example.redis.utils.RedisKeyUtils.getCouponIssueRequestKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueRedisService {

    private final static Long ADD_SUCCESS = 1L;
    private final CouponIssueCache couponIssueCache;
    private final RedisService redisService;

    public void checkCouponIssueQuantityAndDuplicate(CouponRedis coupon, String userId) {
        redisService.execute(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(@NotNull RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();

                Long couponId = coupon.id();
                Long maxQuantity = coupon.maxQuantity();
                log.info("Checking Coupon Issue Quantity and Duplicate For Coupon ID: [{}], User ID: [{}]", couponId, userId);

                String couponIssueRequestKey = getCouponIssueRequestKey(couponId);
                if (!isTotalIssueQuantityAvailable(couponIssueRequestKey, maxQuantity)) {
                    throw new CommonException(COUPON_ISSUE_QUANTITY_EXCEEDED);
                }
                if (isUserAlreadyIssuedCoupon(couponIssueRequestKey, userId)) {
                    throw new CommonException(COUPON_ALREADY_ISSUED_BY_USER);
                }

                return operations.exec();
            }
        });
    }

    private boolean isTotalIssueQuantityAvailable(String issueRequestKey, Long maxQuantity) {
        Long issuedCount = couponIssueCache.getIssuedCouponUserCount(issueRequestKey);
        log.debug("Current Issued Count for Key [{}]: [{}], Max Quantity: [{}]", issueRequestKey, issuedCount, maxQuantity);
        return issuedCount < maxQuantity;
    }

    public boolean isUserAlreadyIssuedCoupon(String issueRequestKey, String userId) {
        Long result = couponIssueCache.addIssuedCouponUser(issueRequestKey, userId);
        log.debug("Attempted to Add User [{}] to Issued Coupon List for Key [{}]: Result: [{}]", userId, issueRequestKey, result);
        return result >= ADD_SUCCESS;
    }

    public Long getIssuedCouponCount(Long couponId) {
        String issuedCountKey = getCouponIssueRequestKey(couponId);
        return couponIssueCache.getIssuedCouponUserCount(issuedCountKey);
    }
}
