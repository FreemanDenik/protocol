package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.mappers.EventMapper;
import com.execute.protocol.core.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public Set<Event> getAllEventDto(){
        return eventRepository.findAll().stream().collect(Collectors.toSet());
    }
    public void saveEvent(Event event){
        eventRepository.save(event);
    }
    public EventDto getRandomEventDto() {
        return EventMapper.INSTANCE.mapEventToDto(eventRepository.findRandomEvent());
    }

    public EventDto getByIdEventDto(long eventId) {
        return EventMapper.INSTANCE.mapEventToDto(eventRepository.findById(eventId).get());
    }

    public Event getById(long eventId) {
        return eventRepository.findById(eventId).get();
    }
}
