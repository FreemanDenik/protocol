package com.execute.protocol.core.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AnswerDto {
    @EqualsAndHashCode.Include
    private int id;
    private boolean useOnce;
    @EqualsAndHashCode.Include
    private String answerText;
}
