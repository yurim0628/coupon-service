package org.example.coupon.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.coupon.domain.Event;
import org.example.coupon.infrastructure.entity.EventEntity;
import org.example.coupon.service.port.EventRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final EventJpaRepository eventJpaRepository;

    @Override
    public Optional<Event> findById(Long eventId) {
        return eventJpaRepository.findById(eventId).map(EventEntity::toModel);
    }
}
