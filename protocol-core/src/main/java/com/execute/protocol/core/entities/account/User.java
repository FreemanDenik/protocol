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
    @Column(name = "add_events")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "PLAYER_ADD_EVENTS")
    private Set<Integer> addEvents;
    @Column(name = "once_events")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "PLAYER_ONCE_EVENTS")
    private Set<Integer> onceEvents;
    @Column(name = "once_answer")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "PLAYER_ONCE_ANSWER")
    private Set<Integer> onceAnswer;
    @Column(name = "things")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "PLAYER_THINGS")
    private Set<Integer> things;
    @Column
    private int currentEvent;
    @Transient
    private byte deadShans = 70;

}
