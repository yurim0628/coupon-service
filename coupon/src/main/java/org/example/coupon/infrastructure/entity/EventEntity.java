package org.example.coupon.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.coupon.domain.Event;

import java.time.LocalDate;
import java.time.LocalTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "events")
@NoArgsConstructor(access = PROTECTED)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    LocalDate startDate;
    LocalDate endDate;
    LocalTime dailyIssueStartTime;
    LocalTime dailyIssueEndTime;

    public static EventEntity fromModel(Event event) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.id = event.id();
        eventEntity.startDate = event.startDate();
        eventEntity.endDate = event.endDate();
        eventEntity.dailyIssueStartTime = event.dailyIssueStartTime();
        eventEntity.dailyIssueEndTime = event.dailyIssueEndTime();
        return eventEntity;
    }

    public Event toModel() {
        return Event.builder()
                .id(id)
                .startDate(startDate)
                .endDate(endDate)
                .dailyIssueStartTime(dailyIssueStartTime)
                .dailyIssueEndTime(dailyIssueEndTime)
                .build();
    }
}
