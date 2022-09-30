package com.execute.protocol.auth.initializations;

import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.acc.Admin;
import com.execute.protocol.core.entities.acc.Role;
import com.execute.protocol.core.entities.acc.User;
import com.execute.protocol.core.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
public class CreateDefaultUsers {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createDefaultUsers() {
        Admin admin =Admin.builder()
                .login("admin")

                .password(passwordEncoder.encode("2174"))
                //.accountCreatedTime(LocalDate.now())
                //.lastAccountActivity(LocalDateTime.now())
                .email("admin@gmail.com")
                // .provider(EnumProviders.PROTOCOL)
                .roles(Set.of(Role.ADMIN))
                .build();
        User user = User.builder()
                        .login("user")
                        .password(passwordEncoder.encode("2174"))
                        //.accountCreatedTime(LocalDate.now())
                        //.lastAccountActivity(LocalDateTime.now())
                        .email("user@gmail.com")
                        .target(Target.builder().money(1).pollution(2).build())
                        .roles(Set.of(Role.USER))
                        .build();
        accountRepository.saveAll(List.of(admin, user));
//        accountRepository.setIdInStringId(admin.getId());
//        accountRepository.setIdInStringId(user.getId());
    }
}
