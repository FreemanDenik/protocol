package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.mappers.EventMapper;
import com.execute.protocol.core.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventDto getRandomEventDto() {
        return EventMapper.INSTANCE.mapEventToDto(eventRepository.findRandomEvent());
    }

    public Event getById(long eventId) {
        return eventRepository.findById(eventId).get();
    }
}
