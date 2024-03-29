package com.execute.protocol.core.entities.account;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACCOUNTS")
@Inheritance(strategy = InheritanceType.JOINED)
public class Account implements UserDetails {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String login;
    @Column
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    // fetch = FetchType.EAGER - необходимо для теста (jwt) именно жадная загрузка ролей
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="roles",
            joinColumns=@JoinColumn(name="user_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    /**
     * Время создания аккаунта
     */
    @Column(name = "ACCOUNT_CREATED_TIME", nullable = false)
    private LocalDate accountCreatedTime;

    /**
     * Время последней активности
     */
    @Column(name = "LAST_ACCOUNT_ACTIVITY", nullable = false)
    private LocalDateTime lastAccountActivity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return login;
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
