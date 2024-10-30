package org.example.coupon.service.port;

import org.example.coupon.domain.Event;

import java.util.Optional;

public interface EventRepository {

    Optional<Event> findById(Long eventId);
}
