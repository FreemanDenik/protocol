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

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public Page<Event> getEventsOrderByUpdateTimeDesc(int page, int pageSize){
        return eventRepository.findAllByOrderByUpdateTimeDesc(PageRequest.of(page,pageSize));
    }
    @Transactional
    public void saveEvent(Event event){
        event.setCreateTime(LocalDateTime.now());
        event.setUpdateTime(LocalDateTime.now());
        eventRepository.save(event);
    }

    /**
     * Получить случайное событие из Общей категории и из коллекции id {@link Category} что переданы
     * @param userId id игрока
     * @param eventId id предыдушего события, его мы сключаем
     * @return {@link EventDto}
     */
    @Override
    public EventDto getRandomEventDto(int userId, int eventId) {
        Event event = eventRepository.findRandomEvent(userId, eventId);
        return EventMapper.INSTANCE.mapEventToDto(eventRepository.findRandomEvent(userId, eventId));
    }
    @Override
    public Event getRandomEvent(int userId, int eventId) {
        return eventRepository.findRandomEvent(userId, eventId);
    }
    /**
     * Проверяет еслить у целевого события целевой ответ
     * @param eventId id события
     * @param answerId id ответа
     * @return boolean
     */
    public Optional<Boolean> isEventHasAnswer(int eventId, int answerId){
        return eventRepository.existsEventByIdAndAnswersIn(eventId, answerId);
    }
    public EventDto getByIdEventDto(int eventId) {
        return EventMapper.INSTANCE.mapEventToDto(eventRepository.findById(eventId).get());
    }
    public Event getByIdEvent(int eventId) {
        return eventRepository.findById(eventId).get();
    }
    public Optional<Event> getById(int eventId) {
        return eventRepository.findById(eventId);
    }
}
