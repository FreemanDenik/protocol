package com.execute.protocol.core.initializations;

import com.execute.protocol.core.entities.acc.Admin;
import com.execute.protocol.core.entities.acc.Role;
import com.execute.protocol.core.entities.acc.User;
import com.execute.protocol.core.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class AdminsDefaultCreator {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean("AdminsDefaultCreator")
    public void adminsDefaultCreator() {
        accountRepository.saveAll(
                List.of(
                Admin.builder()
                        //.firstName("Admin")
                        //.lastName("DefaultAdmin")
                        .login("admin")
                        .password(passwordEncoder.encode("2174"))
                        //.accountCreatedTime(LocalDate.now())
                        //.lastAccountActivity(LocalDateTime.now())
                        .email("admin@gmail.com")
                        // .provider(EnumProviders.PROTOCOL)
                        .roles(Set.of(Role.ADMIN))
                        .build(),

                User.builder()
                        //.firstName("Admin")
                        //.lastName("DefaultAdmin")
                        .login("user")
                        .password(passwordEncoder.encode("2174"))
                        //.accountCreatedTime(LocalDate.now())
                        //.lastAccountActivity(LocalDateTime.now())
                        .email("user@gmail.com")
                        // .provider(EnumProviders.PROTOCOL)
                        .roles(Set.of(Role.USER))
                        .build()));

    }
}
