package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventService {
    Optional<Event> getRandomEvent(int userId, int eventId);
    Optional<Integer> getRandomEventId(int userId, int eventId);
    Optional<Boolean> isEventHasAnswer(int eventId, int answerId);
    Optional<Event> getByIdEvent(int eventId);
}
