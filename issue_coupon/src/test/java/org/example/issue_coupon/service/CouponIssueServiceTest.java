package org.example.issue_coupon.service;

import org.example.coupon.domain.Coupon;
import org.example.coupon.service.CouponService;
import org.example.issue_coupon.domain.CouponIssue;
import org.example.issue_coupon.domain.CouponIssueCreate;
import org.example.issue_coupon.service.port.CouponIssueRepository;
import org.example.redis.domain.CouponRedis;
import org.example.redis.service.CouponIssueRedisService;
import org.example.redis.service.CouponRedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.example.coupon.domain.DiscountType.PERCENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CouponIssueServiceTest {

    @InjectMocks
    private CouponIssueService couponIssueService;

    @Mock
    private CouponService couponService;

    @Mock
    private CouponRedisService couponCacheService;

    @Mock
    private CouponIssueRedisService couponIssueCacheService;

    @Mock
    private CouponIssueRepository couponIssueRepository;

    private CouponIssueCreate couponIssueRequest;
    private Long couponId;
    private Long eventId;
    private Long userId;

    @BeforeEach
    void setUp() {
        couponId = 1L;
        eventId = 1L;
        userId = 1L;
        couponIssueRequest = new CouponIssueCreate(couponId, eventId, userId);
    }

    @Test
    @DisplayName("[SUCCESS] 쿠폰 발급이 정상적으로 이루어지는 경우")
    void issueCoupon_success() {
        // given
        CouponRedis cachedCoupon = new CouponRedis(couponId, 10L, 100L);
        given(couponCacheService.getCoupon(couponId)).willReturn(Optional.of(cachedCoupon));
        willDoNothing().given(couponIssueCacheService).checkCouponIssueQuantityAndDuplicate(cachedCoupon, userId.toString());

        // when
        couponIssueService.issueCoupon(couponIssueRequest);

        // then
        verify(couponCacheService).getCoupon(couponId);
        verify(couponIssueCacheService).checkCouponIssueQuantityAndDuplicate(cachedCoupon, userId.toString());
        verify(couponIssueRepository).save(any(CouponIssue.class));
    }

    @Test
    @DisplayName("[SUCCESS] 캐시에 쿠폰이 없을 경우 DB에서 조회하여 저장")
    void issueCoupon_fetchFromDatabase_whenCouponNotInCache() {
        // given
        Coupon couponFromDb = Coupon.builder()
                .id(couponId)
                .discountType(PERCENT)
                .discountRate(10L)
                .maxQuantity(500L)
                .issuedQuantity(100L)
                .validateStartDate(LocalDateTime.now().minusDays(1))
                .validateEndDate(LocalDateTime.now().plusDays(30))
                .eventId(1L)
                .build();
        given(couponCacheService.getCoupon(couponId)).willReturn(Optional.empty());
        given(couponService.getCoupon(couponId)).willReturn(couponFromDb);

        CouponRedis cachedCoupon = new CouponRedis(couponId, 500L, 1L);
        willDoNothing().given(couponIssueCacheService).checkCouponIssueQuantityAndDuplicate(cachedCoupon, userId.toString());

        // when
        couponIssueService.issueCoupon(couponIssueRequest);

        // then
        verify(couponCacheService).setCoupon(eq(couponId), any(CouponRedis.class));
        verify(couponService).getCoupon(couponId);
        verify(couponIssueRepository).save(any(CouponIssue.class));
    }
}
