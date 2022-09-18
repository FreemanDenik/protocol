package com.execute.protocol.core.entities;

import com.execute.protocol.core.enums.EnumActionTarget;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DOINGS")
public class Doing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @Enumerated(EnumType.STRING)
    private EnumActionTarget actionTarget;
    @Column
    private int valueTarget;
}
