package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.mappers.EventMapper;
import com.execute.protocol.core.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import com.execute.protocol.core.entities.Category;
import java.util.Set;

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

    /**
     * Получить случайное событие из Общей категории и из коллекции id {@link Category} что переданы
     * @param categoryIdSet коллецяи id {@link Category}
     * @return {@link EventDto}
     */
    @Override
    public EventDto getRandomEventDto(Set<Integer> categoryIdSet) {
        return EventMapper.INSTANCE.mapEventToDto(eventRepository.findRandomEvent(categoryIdSet));
    }

    public EventDto getByIdEventDto(int eventId) {
        return EventMapper.INSTANCE.mapEventToDto(eventRepository.findById(eventId).get());
    }

    public Event getById(int eventId) {
        return eventRepository.findById(eventId).get();
    }
}
