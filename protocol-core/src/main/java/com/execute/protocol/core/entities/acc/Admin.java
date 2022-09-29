package com.execute.protocol.core.entities.acc;

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
