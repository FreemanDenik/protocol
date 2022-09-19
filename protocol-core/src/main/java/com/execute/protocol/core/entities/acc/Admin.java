package com.execute.protocol.core.entities.acc;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "ADMIN_ACCOUNTS")
public class Admin extends Account{
    public Admin() {
        super();
    }
}
