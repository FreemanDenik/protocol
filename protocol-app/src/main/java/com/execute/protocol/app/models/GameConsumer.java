package com.execute.protocol.app.models;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameConsumer {
    @Min(message = "событие не может быть меньше 1", value = 1)
    private int event;
    @Min(message = "ответ не может быть меньше 1", value = 1)
    private int answer;
}