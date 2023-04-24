package com.execute.protocol.core.entities;

import com.execute.protocol.core.entities.account.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "TARGETS")
// При JSON сериализациях игнорирует поле user, иначе происходит зацикливание,
// т.е. модель User имеет ссылку на Target
@JsonIgnoreProperties({"user"})
public class Target {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private int gold;
    @Column
    private int reputation;
    @Column
    private int health;
    @Column
    private int fight;
    @Column
    private int thirst;
    @Column
    private int shadow;
    // Обратная связь
    @OneToOne(mappedBy = "target",fetch = FetchType.LAZY)
    private User user;
}
