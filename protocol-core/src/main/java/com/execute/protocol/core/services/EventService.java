package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.mappers.EventMapper;

public interface EventService {
    public EventDto getRandomEventDto();
    public Event getById(long eventId);
}
