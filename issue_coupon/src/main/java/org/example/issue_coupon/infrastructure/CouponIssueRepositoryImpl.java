package org.example.issue_coupon.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.issue_coupon.domain.CouponIssue;
import org.example.issue_coupon.infrastructure.entity.CouponIssueEntity;
import org.example.issue_coupon.service.port.CouponIssueRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepositoryImpl implements CouponIssueRepository {

    private final CouponIssueJpaRepository couponIssueJpaRepository;

    @Override
    public CouponIssue save(CouponIssue couponIssue) {
        return couponIssueJpaRepository.save(CouponIssueEntity.fromModel(couponIssue)).toModel();
    }
}
