package org.example.coupon.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.coupon.domain.Coupon;
import org.example.coupon.infrastructure.entity.CouponEntity;
import org.example.coupon.service.port.CouponRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Optional<Coupon> findById(Long id) {
        return couponJpaRepository.findById(id).map(CouponEntity::toModel);
    }

    @Override
    public List<Coupon> findAll() {
        return couponJpaRepository.findAll()
                .stream()
                .map(CouponEntity::toModel)
                .toList();
    }

    @Override
    public void updateIssuedQuantity(Long id, Long issuedQuantity) {
        couponJpaRepository.updateIssuedQuantity(id, issuedQuantity);
    }
}
