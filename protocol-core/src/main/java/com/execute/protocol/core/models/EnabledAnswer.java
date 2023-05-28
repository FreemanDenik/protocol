package com.execute.protocol.core.models;

import java.util.Set;

/**
 * Модель для {@link com.execute.protocol.core.dto.AnswerDto} разрешенный/запрещенный ответ,
 * указанием целевых предметов
 */
public class EnabledAnswer {
    private Set<String> allow;
    private Set<String> deny;
}
