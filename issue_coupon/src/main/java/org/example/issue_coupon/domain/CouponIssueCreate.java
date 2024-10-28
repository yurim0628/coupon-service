package org.example.issue_coupon.domain;

import jakarta.validation.constraints.NotNull;

public record CouponIssueCreate(
        @NotNull(message = "멤버 ID는 null일 수 없습니다.")
        Long userId,

        @NotNull(message = "이벤트 ID는 null일 수 없습니다.")
        Long eventId,

        @NotNull(message = "쿠폰 ID는 null일 수 없습니다.")
        Long couponId
) {
}
