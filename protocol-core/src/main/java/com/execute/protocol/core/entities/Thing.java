package com.execute.protocol.core.entities;

import com.execute.protocol.core.enums.EmTarget;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "TEMP_THINGS")
public class Thing  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    /**
     * Опубликован ли предмет
     */
    @Column
    private boolean publication;

    @NotNull
    @NotBlank
    @EqualsAndHashCode.Include
    @Column(unique = true)
    private String title;
    @Column
    private String description;
    @Column
    @Enumerated(EnumType.STRING)
    private EmTarget target;  // GOLD     REPUTATION  ANSWER
    @Column
    //почему такое название meaning? Потому что если использовать "value" которое хочется, то падает база H2 и тесты не работают
    private int meaning; // +20       -30         23
}
