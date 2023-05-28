package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.AnswerDto;
import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.repositories.EventRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.execute.protocol.core.entities.Category;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    /**
     * Получить случайное событие из Общей категории и из коллекции id {@link Category} что переданы
     *
     * @param userId  id игрока
     * @param eventId id предыдушего события, его мы сключаем
     * @return {@link EventDto}
     */

    @Override
    //@Cacheable(value = "itemCache")
    public Optional<Event> getRandomEvent(int userId, int eventId) {
        return eventRepository.findRandomEvent(userId, eventId);
    }
    @Override
    public Optional<Integer> getRandomEventId(int userId, int eventId) {
        return eventRepository.findRandomEventId(userId, eventId);
    }

    /**
     * Проверяет есть ли у целевого события целевой ответ
     *
     * @param eventId  id события
     * @param answerId id ответа
     * @return boolean
     */
    public Optional<Boolean> isEventHasAnswer(int eventId, int answerId) {
        return eventRepository.existsEventByIdAndAnswersIn(eventId, answerId);
    }

    /**
     * Получить событие по id
     *
     * @param eventId id события
     * @return {@code Optional<Event>}
     */
    @Override
    @Cacheable(value = "eventAnswerCache")
    public Optional<Event> getByIdEvent(int eventId) {
        return eventRepository.findById(eventId);
    }
}
