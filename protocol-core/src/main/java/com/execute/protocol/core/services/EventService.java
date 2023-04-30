package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface EventService {
    Page<Event> getEventsOrderByUpdateTimeDesc(int page, int pageSize);
    void saveEvent(Event event);
    EventDto getRandomEventDto(Set<Integer> ids);
    EventDto getByIdEventDto(int eventId);
    Event getById(int eventId);
}
