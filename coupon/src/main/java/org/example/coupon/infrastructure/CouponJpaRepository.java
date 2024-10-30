package org.example.coupon.infrastructure;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.coupon.infrastructure.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {

    @Modifying
    @Query("update CouponEntity c set c.issuedQuantity = :issuedQuantity where c.id = :id")
    void updateIssuedQuantity(@Param("id") Long id, @Param("issuedQuantity") Long issuedQuantity);

    List<CouponEntity> findCouponsByEventId(Long eventId);
}
