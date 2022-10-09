package com.execute.protocol.core.initializations;

import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Doing;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.enums.EnumActionTarget;
import com.execute.protocol.core.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Profile("dev")
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
                        .question("Трактирщик наливает всем рома")
                        .answers(Arrays.asList(
                                Answer.builder()
                                        .text("Налей мне полную")
                                        .doing(Arrays.asList(
                                                Doing.builder()
                                                        .actionTarget(EnumActionTarget.THIRST)
                                                        .valueTarget(2).build(),
                                                Doing.builder()
                                                        .actionTarget(EnumActionTarget.SHADOW)
                                                        .valueTarget(-1).build(),
                                                Doing.builder()
                                                        .actionTarget(EnumActionTarget.GOLD)
                                                        .valueTarget(-2).build(),
                                                Doing.builder()
                                                        .actionTarget(EnumActionTarget.REPUTATION)
                                                        .valueTarget(1).build()))
                                        .build(),
                                Answer.builder()
                                        .text("С меня хватит!")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.SHADOW)
                                                                .valueTarget(-1).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.THIRST)
                                                                .valueTarget(-1).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.GOLD)
                                                                .valueTarget(1).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.REPUTATION)
                                                                .valueTarget(-1).build()))
                                        .build(),
                                Answer.builder()
                                        .text("Мне худо от твоего рома заплати мне!")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.GOLD)
                                                                .valueTarget(2).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.SHADOW)
                                                                .valueTarget(-2).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.REPUTATION)
                                                                .valueTarget(-2).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.THIRST)
                                                                .valueTarget(-2).build()))
                                        .build()
                        ))
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build(),
                // Вторая карточка
                Event.builder()
                        .question("Выпивающие за соседним столиков слишком шумят")
                        .answers(Arrays.asList(
                                Answer.builder()
                                        .text("Заплатить чтобы не шумели")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.GOLD)
                                                                .valueTarget(-3).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.FIGHT)
                                                                .valueTarget(-2).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.SHADOW)
                                                                .valueTarget(4).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.REPUTATION)
                                                                .valueTarget(-1).build()
                                                )
                                        )
                                        .build(),
                                Answer.builder()
                                        .text("Попросить чтобы не шумели")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.SHADOW)
                                                                .valueTarget(2).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.FIGHT)
                                                                .valueTarget(-3).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.REPUTATION)
                                                                .valueTarget(-4).build()
                                                )
                                        )
                                        .build(),
                                Answer.builder()
                                        .text("В грубой форме заткнуть их")
                                        .doing(
                                                Arrays.asList(
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.SHADOW)
                                                                .valueTarget(-3).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.FIGHT)
                                                                .valueTarget(3).build(),
                                                        Doing.builder()
                                                                .actionTarget(EnumActionTarget.REPUTATION)
                                                                .valueTarget(2).build()
                                                )
                                        )
                                        .build()
                        ))
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build()
        ));

    }
}
