package com.execute.protocol.core.entities.acc;

import com.execute.protocol.core.entities.Target;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_ACCOUNTS")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class User extends Account{

    @Column
    private LocalDate birthday;
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "target_id", referencedColumnName = "id")
    private Target target;
    @Column
    private long currentEvent;

}