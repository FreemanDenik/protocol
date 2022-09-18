package com.execute.protocol.core.entities;

import com.execute.protocol.core.enums.EnumActionTarget;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ANSWERS")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String text;

    @OneToMany
    @JoinColumn(name = "doing_id", referencedColumnName = "id")
    private Collection<Doing> doing;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_id", referencedColumnName = "id")
    private Event event;
}
