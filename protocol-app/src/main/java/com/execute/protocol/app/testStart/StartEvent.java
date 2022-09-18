package com.execute.protocol.app.testStart;

import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Doing;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.enums.EnumActionTarget;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StartEvent {
    public static List<Event> getEvents(){
        Answer answer1 = Answer.builder()
                .id(1)
                .text("Заставить завод утилизировать 30% отходов")
                .doing(
                        Arrays.asList(
                                Doing.builder()
                                        .id(1)
                                        .actionTarget(EnumActionTarget.MONEY)
                                        .valueTarget(1).build(),
                                Doing.builder()
                                        .id(2)
                                        .actionTarget(EnumActionTarget.POLLUTION)
                                        .valueTarget(-2).build()
                        )
                )
                .build();
        Answer answer2 = Answer.builder()
                .id(2)
                .text("Заставить заводит утилизировать 100% отходов")
                .doing(
                        Arrays.asList(
                                Doing.builder()
                                        .id(3)
                                        .actionTarget(EnumActionTarget.MONEY)
                                        .valueTarget(-2).build(),
                                Doing.builder()
                                        .id(4)
                                        .actionTarget(EnumActionTarget.POLLUTION)
                                        .valueTarget(2).build()
                        )
                )
                .build();
        Answer answer3 = Answer.builder()

                .id(3)
                .text("Все хорошо, ничего страшного не будет")
                .doing(
                        Arrays.asList(
                                Doing.builder()
                                        .id(5)
                                        .actionTarget(EnumActionTarget.MONEY)
                                        .valueTarget(2).build(),
                                Doing.builder()
                                        .id(6)
                                        .actionTarget(EnumActionTarget.POLLUTION)
                                        .valueTarget(-4).build()
                        )
                )
                .build();
        Event event1 = Event.builder()
                .id(1)
                .question("Заводы сбрасывают слишком много отходов, с этим надо что то делать!")
                .answers(Arrays.asList(answer1, answer2, answer3))
                .build();

        Answer answer11 = Answer.builder()
                .id(4)
                .text("Подкупить местных чтобы молчали")
                .doing(
                        Arrays.asList(
                                Doing.builder()
                                        .id(7)
                                        .actionTarget(EnumActionTarget.MONEY)
                                        .valueTarget(2).build(),
                                Doing.builder()
                                        .id(8)
                                        .actionTarget(EnumActionTarget.POLLUTION)
                                        .valueTarget(-3).build()
                        )
                )
                .build();
        Answer answer22 = Answer.builder()
                .id(5)
                .text("Откачать ее до суха, не важно кто будет не доволен")
                .doing(
                        Arrays.asList(
                                Doing.builder()
                                        .id(9)
                                        .actionTarget(EnumActionTarget.MONEY)
                                        .valueTarget(5).build(),
                                Doing.builder()
                                        .id(10)
                                        .actionTarget(EnumActionTarget.POLLUTION)
                                        .valueTarget(-3).build()
                        )
                )
                .build();
        Answer answer33 = Answer.builder()
                .id(6)
                .text("Не трогать! Не будет травить людей, пересели их и через годика 2-4 начнем качать когда рядом не кого не будет")
                .doing(
                        Arrays.asList(
                                Doing.builder()
                                        .id(11)
                                        .actionTarget(EnumActionTarget.MONEY)
                                        .valueTarget(-1).build(),
                                Doing.builder()
                                        .id(12)
                                        .actionTarget(EnumActionTarget.POLLUTION)
                                        .valueTarget(1).build()
                        )
                )
                .build();
        Event event11 = Event.builder()
                .id(2)
                .question("Нашли месторождение нефти, но рядом живут желые городки и села")
                .answers(Arrays.asList(answer11, answer22, answer33))
                .build();

        return Arrays.asList(event1, event11);
    }
}
