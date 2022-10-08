package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;

import java.util.Set;

public interface EventService {
    Set<Event> getAllEventDto();
    void saveEvent(Event event);
    EventDto getRandomEventDto();
    EventDto getByIdEventDto(long eventId);
    Event getById(long eventId);
}
