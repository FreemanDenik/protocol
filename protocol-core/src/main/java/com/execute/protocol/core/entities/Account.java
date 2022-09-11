package com.execute.protocol.core.entities;

import com.execute.protocol.core.enums.EnumProviders;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
@NoArgsConstructor
@SuperBuilder
@Entity
@Getter
@Setter
@Table(name = "ACCOUNTS")
@Inheritance(strategy = InheritanceType.JOINED)
public class Account implements UserDetails {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Имя
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * Фамилия
     */
    @Column(nullable = false)
    private String lastName;
    @Column(length = 128, nullable = false)
    private String username;
    @Column
    private String password;
    @Column(nullable = false)
    private String email;
    @Column
    private Role role;
    @Column
    @Enumerated(EnumType.STRING)
    private EnumProviders provider;
    /**
     * Время создания аккаунта
     */
    @Column(nullable = false)
    private LocalDate accountCreatedTime;

    /**
     * Время последней активности
     */
    @Column( nullable = false)
    private LocalDateTime lastAccountActivity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
