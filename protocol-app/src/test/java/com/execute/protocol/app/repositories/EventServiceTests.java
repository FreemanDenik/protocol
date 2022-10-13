package com.execute.protocol.app.repositories;

import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.repositories.EventRepository;
import com.execute.protocol.core.services.EventService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
// Эта она анотация отключает использование базы данных в памяти как H2 и использует текущие настройки
// т.е. MySql
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

// Аннотация позволяет использовать аннотации Before и After в не статическом виде
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@DataJpaTest
class EventServiceTests {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;

    @BeforeAll
    void setEvents() {
        eventRepository.saveAll(
                List.of(
                        Event.builder().question("question 1").updateTime(LocalDateTime.now().plusSeconds(1)).createTime(LocalDateTime.now()).build(),
                        Event.builder().question("question 2").updateTime(LocalDateTime.now().plusSeconds(2)).createTime(LocalDateTime.now()).build(),
                        Event.builder().question("question 3").updateTime(LocalDateTime.now().plusSeconds(3)).createTime(LocalDateTime.now()).build(),
                        Event.builder().question("question 4").updateTime(LocalDateTime.now().plusSeconds(4)).createTime(LocalDateTime.now()).build(),
                        Event.builder().question("question 5").updateTime(LocalDateTime.now().plusSeconds(5)).createTime(LocalDateTime.now()).build()
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
        "1, question 5",
        "2, question 5",
        "3, question 5",
        "4, question 5",
        "5, question 5"
    })
   // @ValueSource(ints = {5, 4, 3, 2, 1}, strings = {"question 5"})
    void findAllByOrderByUpdateTimeDescTest(int pageSize, String compare) {
        assertNotNull(eventService);
        Page<Event> page = eventService.getEventsOrderByUpdateTimeDesc(0, pageSize);

        List<Event> events = page.getContent();
        assertEquals(events.get(0).getQuestion(), compare);
        assertEquals(events.size(), pageSize);
    }
}