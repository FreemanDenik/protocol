package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import org.springframework.data.domain.Page;

public interface EventService {
    Page<Event> getEventsOrderByUpdateTimeDesc(int page, int pageSize);
    void saveEvent(Event event);
    EventDto getRandomEventDto();
    EventDto getByIdEventDto(long eventId);
    Event getById(long eventId);
}
