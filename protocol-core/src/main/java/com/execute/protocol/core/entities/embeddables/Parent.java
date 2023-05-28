package com.execute.protocol.core.entities.embeddables;

import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Parent implements Serializable {
    /**
     * Главный родительское событие (с которого начинается сюжет)
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_event")
    private Event parentEvent;
    /**
     * Не посредственно родительское событие
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "own_event")
    private Event ownEvent;
    /**
     * Ответ, который приводит к этому событию
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "own_answer")
    private Answer ownAnswer;
}
