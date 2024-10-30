package org.example.coupon.service;

import org.example.coupon.domain.Coupon;
import org.example.coupon.domain.CouponResponse;
import org.example.coupon.domain.DiscountType;
import org.example.coupon.service.port.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.example.coupon.domain.DiscountType.AMOUNT;
import static org.example.coupon.domain.DiscountType.PERCENT;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Test
    @DisplayName("이벤트 ID로 쿠폰을 조회하면 올바른 쿠폰이 반환되어야 한다")
    public void testGetCoupons() {
        // given
        Long eventId = 1L;
        Coupon coupon1 = Coupon.builder()
                .id(1L)
                .discountType(PERCENT)
                .discountRate(10L)
                .validateStartDate(LocalDateTime.now())
                .validateEndDate(LocalDateTime.now().plusDays(10))
                .build();
        Coupon coupon2 = Coupon.builder()
                .id(2L)
                .discountType(AMOUNT)
                .discountPrice(20L)
                .validateStartDate(LocalDateTime.now())
                .validateEndDate(LocalDateTime.now().plusDays(10))
                .build();
        List<Coupon> coupons = Stream.of(coupon1, coupon2).collect(Collectors.toList());
        given(couponRepository.findCouponsByEventId(eventId)).willReturn(coupons);

        // when
        List<CouponResponse> couponResponses = couponService.getCoupons(eventId);

        // then
        then(couponResponses.size()).isEqualTo(2);
        then(couponResponses.get(0).id()).isEqualTo(coupon1.id());
        then(couponResponses.get(1).id()).isEqualTo(coupon2.id());
    }
}
