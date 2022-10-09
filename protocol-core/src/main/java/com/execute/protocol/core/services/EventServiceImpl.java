package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.mappers.EventMapper;
import com.execute.protocol.core.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public Page<Event> getEventsOrderByUpdateTimeDesc(int page, int pageSize){
        return eventRepository.findAllByOrderByUpdateTimeDesc(PageRequest.of(page,pageSize));
    }
    public void saveEvent(Event event){
        event.setCreateTime(LocalDateTime.now());
        event.setUpdateTime(LocalDateTime.now());
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
