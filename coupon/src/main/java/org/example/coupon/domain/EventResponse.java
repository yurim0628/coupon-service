package org.example.coupon.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventResponse(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime dailyIssueStartTime,
        LocalTime dailyIssueEndTime
) {
    public static EventResponse of(Event event) {
        return new EventResponse(
                event.id(),
                event.startDate(),
                event.endDate(),
                event.dailyIssueStartTime(),
                event.dailyIssueEndTime()
        );
    }
}
