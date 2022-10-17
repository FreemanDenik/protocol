package com.execute.protocol.core.entities.account;

import com.execute.protocol.core.entities.Target;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_ACCOUNTS")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class User extends Account{

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "target_id", referencedColumnName = "id")
    private Target target;
    @Column
    private long currentEvent;

}
