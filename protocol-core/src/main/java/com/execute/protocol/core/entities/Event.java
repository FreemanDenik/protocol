package com.execute.protocol.core.entities;

import com.execute.protocol.core.entities.embeddables.Parent;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "TEMP_EVENTS")
// При JSON сериализациях игнорирует поле user, иначе происходит зацикливание,
// т.е. модель Answer имеет ссылку на Event
//@JsonIgnoreProperties({"answers"})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    /**
     * Опубликована ли карта (для администрирования)
     */
    @Column
    private boolean publication;
    /**
     * Встроенная таблица родительских событий и ответов
     */
    @Embedded
    private Parent parent;
    /**
     * Является ли карта дочерней
     */
    @Column
    private boolean child;
    /**
     * Включена карточка или отключена (для игрового процесса)
     */
    @Column
    private boolean shadow;
    /**
     * используется событие многократно или однократно
     */
    @Column
    private boolean useOnce;
    @Column
    private int category;
    @Column
    @NotNull
    @NotBlank
    @EqualsAndHashCode.Include
    private String eventText;
    @Column
    private String description;
    @Column(name = "create_time")
    @NotNull
    private LocalDateTime createTime;
//    @OneToMany
//    @JoinColumn(name = "event_id")
//    private Set<OpenEvent> openEvents;
    @Column(name = "update_time")
    @NotNull
    private LocalDateTime updateTime;
    @Column(nullable = false)
    @NotNull
    @OneToMany(
            mappedBy = "event",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    //CascadeType.REMOVE представляет собой способ удаления дочерней сущности или сущностей, когда происходит удаление ее родителя
    //Если orphanRemoval=true, то при удалении комментария из списка комментариев топика, комментарий удаляется из базы
    //Если orphanRemoval=false, то при удалении комментария из списка, в базе комментарий остается.  Просто его внешний ключ (comment.topic_id) обнуляется, и больше комментарий не ссылается на топик.
    //orphanRemoval дает нам возможность удалять потерянные объекты из базы данных
    @OrderBy("id ASC")
    private Set<Answer> answers = new LinkedHashSet<>();

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
        answer.setEvent(null);
    }
    public void syncAnswers() {
        answers.forEach(w -> w.setEvent(this));
    }

}
