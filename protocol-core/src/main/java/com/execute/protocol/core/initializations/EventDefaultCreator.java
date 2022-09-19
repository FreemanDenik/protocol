package com.execute.protocol.core.initializations;

import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Doing;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.enums.EnumActionTarget;
import com.execute.protocol.core.repositories.EventRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EventDefaultCreator {
    private final EventRepository eventRepository;

    @Autowired
    public EventDefaultCreator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Bean("EventDefaultCreator")
    public void eventDefaultCreator() {

        eventRepository.saveAll(Arrays.asList(
                // Первая карточка
                Event.builder()
                        .question("Заводы сбрасывают слишком много отходов, с этим надо что то делать!")
                        .answers(Arrays.asList(
                                Answer.builder()
                                        .text("Заставить завод утилизировать 30% отходов")
                                        .doing(Arrays.asList(
                                                Doing.builder()
                                                        .actionTarget(EnumActionTarget.MONEY)
                                                        .valueTarget(-2).build(),
                                                Doing.builder()
                                                        .actionTarget(EnumActionTarget.POLLUTION)
                                                        .valueTarget(3).build()))
                                        .build(),
                                Answer.builder()
                                        .text("Заставить заводит утилизировать 100% отходов")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.MONEY)
                                                                .valueTarget(-2).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.POLLUTION)
                                                                .valueTarget(2).build()))
                                        .build(),
                                Answer.builder()
                                        .text("Заставить заводит утилизировать 100% отходов")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.MONEY)
                                                                .valueTarget(-2).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.POLLUTION)
                                                                .valueTarget(2).build()))
                                        .build()
                        ))
                        .build(),
                // Вторая карточка
                Event.builder()
                        .question("Нашли месторождение нефти, но рядом живут жилые городки и села")
                        .answers(Arrays.asList(
                                Answer.builder()
                                        .text("Все хорошо, ничего страшного не будет")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.MONEY)
                                                                .valueTarget(2).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.POLLUTION)
                                                                .valueTarget(-4).build()
                                                )
                                        )
                                        .build(),
                                Answer.builder()
                                        .text("Подкупить местных чтобы молчали")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.MONEY)
                                                                .valueTarget(2).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.POLLUTION)
                                                                .valueTarget(-3).build()
                                                )
                                        )
                                        .build(),
                                Answer.builder()
                                        .text("Не трогать! Не будет травить людей, пересели их и через годика 2-4 начнем качать когда рядом не кого не будет")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.MONEY)
                                                                .valueTarget(-1).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.POLLUTION)
                                                                .valueTarget(1).build()
                                                )
                                        )
                                        .build()
                        ))
                        .build()
        ));

    }
}
