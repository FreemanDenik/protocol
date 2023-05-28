package com.execute.protocol.core.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AnswerDto {
    @EqualsAndHashCode.Include
    private int id;
    private boolean useOnce;
    @EqualsAndHashCode.Include
    private String answerText;
    @Builder.Default
    private boolean enabled = true;
    private Set<String> haveThings;
    private Set<String> needThings;
    private String description;
}
