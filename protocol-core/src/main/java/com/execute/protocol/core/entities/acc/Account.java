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

    private long id;
    @Column(name = "string_id", unique = true)
    private String stringId;
    @Column
    private String login;
    @Column
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @ElementCollection
    @CollectionTable(
            name="roles",
            joinColumns=@JoinColumn(name="user_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

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
