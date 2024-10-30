package org.example.coupon.domain;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record Event(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime dailyIssueStartTime,
        LocalTime dailyIssueEndTime
) {
}
