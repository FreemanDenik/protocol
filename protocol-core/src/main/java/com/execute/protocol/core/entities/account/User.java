package com.execute.protocol.core.entities.account;

import com.execute.protocol.core.entities.Target;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_ACCOUNTS")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends Account{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "target_id", referencedColumnName = "id")
    private Target target;
    @Column(name = "add_categories")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "PLAYER_ADD_CATEGORIES")
    private Set<Integer> addCategories;
    @Column
    private int currentEvent;

}
