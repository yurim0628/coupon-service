package org.example.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.common.exception.CommonException;
import org.example.coupon.domain.Event;
import org.example.coupon.domain.EventResponse;
import org.example.coupon.service.port.EventRepository;
import org.springframework.stereotype.Service;

import static org.example.common.exception.ErrorCode.EVENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponse getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CommonException(EVENT_NOT_FOUND));
        return EventResponse.of(event);
    }
}
