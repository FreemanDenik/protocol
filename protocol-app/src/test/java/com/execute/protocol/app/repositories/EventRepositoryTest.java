package com.execute.protocol.app.repositories;

import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.repositories.EventRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
// Эта она анотация отключает использование базы данных в памяти как H2 и использует текущие настройки
// т.е. MySql
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;


    @Test
    void findRandomEvent() {
        assertNotNull(eventRepository);
        List<Event> events1 = List.of(
                Event.builder().question("qwerty 1").updateTime(LocalDateTime.now().plusSeconds(1)).createTime(LocalDateTime.now()).build(),
                Event.builder().question("qwerty 2").updateTime(LocalDateTime.now().plusSeconds(2)).createTime(LocalDateTime.now()).build(),
                Event.builder().question("qwerty 3").updateTime(LocalDateTime.now().plusSeconds(3)).createTime(LocalDateTime.now()).build(),
                Event.builder().question("qwerty 4").updateTime(LocalDateTime.now().plusSeconds(2)).createTime(LocalDateTime.now()).build()
        );
        eventRepository.saveAll(events1);
        Page<Event> events = eventRepository.findAllByOrderByUpdateTimeDesc(Pageable.ofSize(3));
        List<Event> e = events.getContent();
        assertEquals(e.get(0).getQuestion(), "qwerty 3");
        assertEquals(e.size(), 3);
        //eventRepository.deleteAll(events1);
    }
}