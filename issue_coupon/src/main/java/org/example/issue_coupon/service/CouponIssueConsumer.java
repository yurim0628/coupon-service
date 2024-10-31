package org.example.issue_coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.issue_coupon.domain.CouponIssue;
import org.example.issue_coupon.domain.CouponIssueCreate;
import org.example.issue_coupon.service.port.CouponIssueRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponIssueRepository couponIssueRepository;

    @KafkaListener(topics = "topic", groupId = "coupon_issue_consumer_group")
    public void listener(CouponIssueCreate couponIssueCreate) {
        saveCouponIssue(couponIssueCreate);
    }

    private void saveCouponIssue(CouponIssueCreate couponIssueCreate) {
        CouponIssue couponIssue = CouponIssue.from(couponIssueCreate);
        couponIssueRepository.save(couponIssue);
    }
}
