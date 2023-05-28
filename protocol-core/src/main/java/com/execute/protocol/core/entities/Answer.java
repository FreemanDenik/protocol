package com.execute.protocol.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "TEMP_ANSWERS")
@JsonIgnoreProperties({"event","linkEvent"})
public class Answer  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @Column(name = "use_once")
    private boolean useOnce;
    @NotBlank
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "answer_text", nullable = false)
    private String answerText;
    @Column
    private String description;
    @Column
    private byte gold;
    @Column
    private byte reputation;
    @Column
    private byte influence;
    @Column
    private byte shadow;
    @Column
    private byte luck;
    // не давать название addThinks mupstruct пропускает такие поля
    /**
     * дать предмет
     */
    @Column(name = "give_things")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TEMP_GIVE_THINGS")
    private Set<Integer> giveThings;
    /**
     * Забрать предмет
     */
    @Column(name = "take_things")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TEMP_TAKE_THINGS")
    private Set<Integer> takeThings;
    @Column(name = "if_things")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TEMP_IF_THINGS")
    private Set<Integer> ifThings;
    @Column(name = "open_event")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TEMP_OPEN_EVENT")
    private Set<Integer> openCarts;
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "link_event", referencedColumnName = "id")
    private Event linkEvent;
    // Кеширование
    @Cacheable(value = "eventAnswerCache")
    public Event getLinkEvent() {
        return linkEvent;
    }
    @Column(name = "open_categories")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TEMP_OPEN_CATEGORIES")
    private Set<Integer> openCategories;
    @Column(name = "close_categories")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TEMP_CLOSE_CATEGORIES")
    private Set<Integer> closeCategories;
    // optional = false,  Если установлено значение false, то всегда должна существовать ненулевая связь.
    // @JoinColumnработает с физической моделью, т. е. с тем, как вещи фактически расположены в хранилище данных (базе данных). Указание nullable = falseсделает столбец БД необнуляемым.
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;
}
