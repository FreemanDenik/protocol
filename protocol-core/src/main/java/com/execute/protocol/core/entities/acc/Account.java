package com.execute.protocol.core.entities.acc;

import com.execute.protocol.core.enums.EnumProviders;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACCOUNTS")
public class Account extends AuthUser {


    @Column
    private Long clientId;
    @Column
    @Enumerated(EnumType.STRING)
    private EnumProviders provider;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    /**
     * Время создания аккаунта
     */
    @Column(nullable = false)
    private LocalDate accountCreatedTime;
    /**
     * Время последней активности
     */
    @Column(nullable = false)
    private LocalDateTime lastAccountActivity;



}
