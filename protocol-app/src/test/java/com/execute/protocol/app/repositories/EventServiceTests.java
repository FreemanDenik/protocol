package com.execute.protocol.app.repositories;

import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.repositories.EventRepository;
import com.execute.protocol.core.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
// Эта она аннотация отключает использование базы данных в памяти как H2 и использует текущие настройки
// т.е. для этого проекта это MySql
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

// Аннотация позволяет использовать аннотации BeforeAll и AfterAll в не статическом виде
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@SpringBootTest
// Аннотация подтягивает тестовые бд, для mysql jpa в данном случаи используется база H2,
// для redis используется тот же redis только создается отдельный сервер,
// заменяет
// @DataJpaTest
// @DataRedisTest
@AutoConfigureTestDatabase
class EventServiceTests {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;

    @BeforeEach
    void setEvents() {
        eventRepository.saveAll(
                List.of(
                        Event.builder().eventText("question 1").updateTime(LocalDateTime.now().plusSeconds(1)).createTime(LocalDateTime.now()).build(),
                        Event.builder().eventText("question 2").updateTime(LocalDateTime.now().plusSeconds(2)).createTime(LocalDateTime.now()).build(),
                        Event.builder().eventText("question 3").updateTime(LocalDateTime.now().plusSeconds(3)).createTime(LocalDateTime.now()).build(),
                        Event.builder().eventText("question 4").updateTime(LocalDateTime.now().plusSeconds(4)).createTime(LocalDateTime.now()).build(),
                        Event.builder().eventText("question 5").updateTime(LocalDateTime.now().plusSeconds(5)).createTime(LocalDateTime.now()).build()
                )
        );
    }

}