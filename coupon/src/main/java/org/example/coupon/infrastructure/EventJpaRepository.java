package org.example.coupon.infrastructure;

import org.example.coupon.infrastructure.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
}
