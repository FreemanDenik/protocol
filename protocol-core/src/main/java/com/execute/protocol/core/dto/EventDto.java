package com.execute.protocol.core.dto;

import com.execute.protocol.core.entities.Category;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventDto {
    @EqualsAndHashCode.Include
    private int id;
    private boolean useOnce;
    private boolean shadow;
    private boolean child;
    @EqualsAndHashCode.Include
    private String eventText;
    private String description;
    private String image;
    private Set<AnswerDto> answers;
}
