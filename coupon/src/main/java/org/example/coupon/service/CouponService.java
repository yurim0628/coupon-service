package org.example.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.common.exception.CommonException;
import org.example.coupon.domain.Coupon;
import org.example.coupon.service.port.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.common.exception.ErrorCode.COUPON_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new CommonException(COUPON_NOT_FOUND));
    }

    public List<Coupon> getCoupons() {
        return couponRepository.findAll();
    }
}
