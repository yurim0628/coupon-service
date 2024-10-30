package org.example.coupon.service;

import org.example.common.exception.CommonException;
import org.example.coupon.domain.Event;
import org.example.coupon.domain.EventResponse;
import org.example.coupon.service.port.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.example.common.exception.ErrorCode.EVENT_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Test
    @DisplayName("[SUCCESS] 이벤트 ID로 이벤트 조회 성공 시 올바른 이벤트가 반환되어야 한다")
    public void testGetEventSuccess() {
        // Given
        Long eventId = 1L;
        Event event = Event.builder()
                .id(eventId)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .dailyIssueStartTime(LocalTime.of(9, 0))
                .dailyIssueEndTime(LocalTime.of(18, 0))
                .build();
        given(eventRepository.findById(eventId)).willReturn(Optional.of(event));

        // When
        EventResponse eventResponse = eventService.getEvent(eventId);

        // Then
        then(eventResponse.id()).isEqualTo(event.id());
        then(eventResponse.startDate()).isEqualTo(event.startDate());
        then(eventResponse.endDate()).isEqualTo(event.endDate());
        then(eventResponse.dailyIssueStartTime()).isEqualTo(event.dailyIssueStartTime());
        then(eventResponse.dailyIssueEndTime()).isEqualTo(event.dailyIssueEndTime());
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    @DisplayName("[ERROR] 존재하지 않는 이벤트 ID로 조회 시 예외가 발생해야 한다")
    public void testGetEventNotFound() {
        // Given
        Long eventId = 1L;
        given(eventRepository.findById(eventId)).willReturn(Optional.empty());

        // When & Then
        thenThrownBy(() -> eventService.getEvent(eventId))
                .isInstanceOf(CommonException.class)
                .extracting("errorCode")
                .isEqualTo(EVENT_NOT_FOUND);
        verify(eventRepository, times(1)).findById(eventId);
    }
}
