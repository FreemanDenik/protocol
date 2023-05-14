package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventService {
    Page<Event> getEventsOrderByUpdateTimeDesc(int page, int pageSize);
    void saveEvent(Event event);
    EventDto getRandomEventDto(int userId, int eventId);
    Event getRandomEvent(int userId, int eventId);
    Optional<Boolean> isEventHasAnswer(int eventId, int answerId);
    EventDto getByIdEventDto(int eventId);
    Event getByIdEvent(int eventId);
    Optional<Event> getById(int eventId);
}
