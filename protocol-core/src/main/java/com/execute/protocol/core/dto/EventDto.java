package com.execute.protocol.core.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class EventDto {
    private long id;
    private String question;
    private Collection<AnswerDto> answers;
}
