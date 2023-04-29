package com.execute.protocol.core.entities.embeddables;

import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Parent {
    /**
     * Главный родительское событие (с которого начинается сюжет)
     */
    @OneToOne
    @JoinColumn(name = "parent_event")
    private Event parentEvent;
    /**
     * Не посредственно родительское событие
     */
    @OneToOne
    @JoinColumn(name = "own_event")
    private Event ownEvent;
    /**
     * Ответ, который приводит к этому событию
     */
    @OneToOne
    @JoinColumn(name = "own_answer")
    private Answer ownAnswer;
}
