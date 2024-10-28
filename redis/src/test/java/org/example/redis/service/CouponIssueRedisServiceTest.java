package org.example.redis.service;

import org.example.common.exception.CommonException;
import org.example.redis.domain.CouponRedis;
import org.example.redis.service.port.CouponIssueCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.example.common.exception.ErrorCode.COUPON_ALREADY_ISSUED_BY_USER;
import static org.example.common.exception.ErrorCode.COUPON_ISSUE_QUANTITY_EXCEEDED;
import static org.example.redis.utils.RedisKeyUtils.getCouponIssueRequestKey;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;

@ExtendWith(MockitoExtension.class)
public class CouponIssueRedisServiceTest {

    @InjectMocks
    private CouponIssueRedisService couponIssueRedisService;

    @Mock
    private CouponIssueCache couponIssueCache;

    @Mock
    private RedisService redisService;

    @Mock
    private RedisOperations<String, Object> redisOperations;

    @Test
    @DisplayName("[SUCCESS] 사용자가 쿠폰을 정상 발급받을 수 있다.")
    void issueCoupon_success() {
        // given
        CouponRedis coupon = createCoupon();
        String userId = "2L";
        String issueRequestKey = getCouponIssueRequestKey(1L);

        given(couponIssueCache.getIssuedCouponUserCount(issueRequestKey)).willReturn(1L);
        given(couponIssueCache.addIssuedCouponUser(issueRequestKey, userId)).willReturn(0L);
        setupRedisServiceExecuteMock();

        // when & then
        assertDoesNotThrow(() -> couponIssueRedisService.checkCouponIssueQuantityAndDuplicate(coupon, userId));
    }

    @Test
    @DisplayName("[ERROR] 쿠폰 발급 가능 수량이 존재하지 않는다면, 예외를 반환한다.")
    void issueCoupon_fail_with_run_out_of_coupon() {
        // given
        CouponRedis coupon = createCoupon();
        String userId = "1L";
        String issueRequestKey = getCouponIssueRequestKey(1L);

        given(couponIssueCache.getIssuedCouponUserCount(issueRequestKey))
                .willReturn(5L);
        setupRedisServiceExecuteMock();

        // when & then
        thenThrownBy(() -> couponIssueRedisService.checkCouponIssueQuantityAndDuplicate(coupon, userId))
                .isInstanceOf(CommonException.class)
                .hasFieldOrPropertyWithValue("errorCode", COUPON_ISSUE_QUANTITY_EXCEEDED);
    }

    @Test
    @DisplayName("[ERROR] 사용자가 이미 쿠폰을 발급받았다면, 예외를 반환한다.")
    void issueCoupon_fail_with_already_issued_by_user() {
        // given
        CouponRedis coupon = createCoupon();
        String userId = "1L";
        String issueRequestKey = getCouponIssueRequestKey(1L);

        given(couponIssueCache.getIssuedCouponUserCount(issueRequestKey))
                .willReturn(1L);
        given(couponIssueCache.addIssuedCouponUser(issueRequestKey, userId))
                .willReturn(1L);
        setupRedisServiceExecuteMock();

        // when & then
        thenThrownBy(() -> couponIssueRedisService.checkCouponIssueQuantityAndDuplicate(coupon, userId))
                .isInstanceOf(CommonException.class)
                .hasFieldOrPropertyWithValue("errorCode", COUPON_ALREADY_ISSUED_BY_USER);
    }

    private void setupRedisServiceExecuteMock() {
        willAnswer(invocation -> {
            SessionCallback<?> sessionCallback = invocation.getArgument(0);
            return sessionCallback.execute(redisOperations);
        }).given(redisService).execute(any(SessionCallback.class));
    }

    private CouponRedis createCoupon() {
        return new CouponRedis(1L, 5L, 1L);
    }
}
