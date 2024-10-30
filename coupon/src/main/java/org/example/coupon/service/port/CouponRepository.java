package org.example.coupon.service.port;

import org.example.coupon.domain.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {

    Optional<Coupon> findById(Long id);

    List<Coupon> findAll();

    void updateIssuedQuantity(Long id, Long issuedQuantity);

    List<Coupon> findCouponsByEventId(Long eventId);
}
