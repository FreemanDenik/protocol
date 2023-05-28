package com.execute.protocol.app;

import com.execute.protocol.core.dto.*;
import com.execute.protocol.core.entities.*;
import com.execute.protocol.core.enums.EmTarget;

import java.util.*;

public class InitializationTests {

    public static Set<Category> getTreeCategory() {
        return Set.of(
                Category.builder().title("1 title category 1").description("description category 1").publication(true).build(),
                Category.builder().title("2 title category 2").description("description category 2").publication(true).build(),
                Category.builder().title("3 title category 3").description("description category 2").publication(true).build()
        );
    }

    public static Set<Thing> getFourThing() {
        return Set.of(
                Thing.builder().title("1 title thing 1").description("description thing 1").publication(true).target(EmTarget.GOLD).meaning(10).build(),
                Thing.builder().title("2 title thing 2").description("description thing 2").publication(true).target(EmTarget.LUCK).meaning(-20).build(),
                Thing.builder().title("3 title thing 3").description("description thing 3").publication(true).target(EmTarget.INFLUENCE).meaning(30).build(),
                Thing.builder().title("4 title thing 4").description("description thing 4").publication(true).target(EmTarget.REPUTATION).meaning(-40).build()
        );
    }

    public static Map<Integer, EventDto> getMapTreeEventDto() {
        return new HashMap<>(Map.of(
                1, EventDto.builder()
                        .eventText("event text 1")
                        .answers(new LinkedHashSet<>(Arrays.asList(
                                AnswerDto.builder().answerText("event text 1 answer text 1").description("description 1").enabled(false).haveThings(Set.of("предмет 1")).needThings(Set.of("d")).build()
                          ))).build(),
                2, EventDto.builder()
                        .eventText("event text 2")
                        .answers(new LinkedHashSet<>(Arrays.asList(
                                AnswerDto.builder().answerText("event text 2 answer text 1").description("description 2").enabled(true).needThings(Set.of("предмет 2")).build()
                                ))).build(),
                3, EventDto.builder()
                        .eventText("event text 3")
                        .answers(new LinkedHashSet<>(Arrays.asList(
                                AnswerDto.builder().answerText("event text 3 answer text 1").description("description 3").enabled(true).build()
                                ))).build())

        );
    }

    public static Set<EventDto> getSetTreeEventDto() {
        return new LinkedHashSet<>(Set.of(
                EventDto.builder()
                        .eventText("event text 1")
                        .answers(new LinkedHashSet<>(Arrays.asList(
                                AnswerDto.builder().answerText("event text 1 answer text 1").description("description 1").enabled(false).haveThings(Set.of("предмет 1")).needThings(Set.of("d")).build()
                                ))).build(),
                EventDto.builder()
                        .eventText("event text 2")
                        .answers(new LinkedHashSet<>(Arrays.asList(
                                AnswerDto.builder().answerText("event text 2 answer text 1").description("description 1").enabled(true).needThings(Set.of("предмет 2")).build()
                                ))).build(),
                EventDto.builder()
                        .eventText("event text 3")
                        .answers(new LinkedHashSet<>(Arrays.asList(
                                AnswerDto.builder().answerText("event text 3 answer text 1").description("description 1").enabled(true).build()
                                ))).build())
        );
    }
}
