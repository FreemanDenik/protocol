package com.execute.protocol.core.entities;

import com.execute.protocol.core.entities.account.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Random;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PLAYER_TARGETS")
// При JSON сериализациях игнорирует поле user, иначе происходит зацикливание,
// т.е. модель User имеет ссылку на Target
@JsonIgnoreProperties({"user"})
public class Target  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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
    // Обратная связь
    @OneToOne(mappedBy = "target", fetch = FetchType.LAZY)
    private User user;
    public static Target generate(){
        Random random = new Random();
        return Target.builder()
                .gold((byte) random.nextInt(10,30))
                .reputation((byte) random.nextInt(10,30))
                .influence((byte) random.nextInt(10,30))
                .shadow((byte) random.nextInt(10,30))
                .luck((byte) random.nextInt(10,30))
                .build();
    }
    public void calcGld(byte gold){
        this.gold += gold;
    }
    public void calcRep(byte reputation){
        this.reputation += reputation;
    }
    public void calcInf(byte influence){
        this.influence += influence;
    }
    public void calcShd(byte shadow){
        this.shadow += shadow;
    }
    public void calcLck(byte luck){
        this.luck += luck;
    }
    @Override
    public String toString(){
        return String.format("id: %d, gold: %d, reputation: %d, influence: %d, shadow: %d, luck: %d", id, gold, reputation, influence, shadow, luck);
    }
}
