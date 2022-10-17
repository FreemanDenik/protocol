package com.execute.protocol.core.entities.account;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "ADMIN_ACCOUNTS")
public class Admin extends Account{


}
