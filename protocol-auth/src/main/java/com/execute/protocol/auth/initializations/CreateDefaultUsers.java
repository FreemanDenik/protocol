package com.execute.protocol.auth.initializations;

import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.account.Admin;
import com.execute.protocol.core.entities.account.Role;
import com.execute.protocol.core.entities.account.User;
import com.execute.protocol.core.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class CreateDefaultUsers {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createDefaultUsers() {
        Random random = new Random();
        Admin admin = Admin.builder()
                .login("admin")
                .password(passwordEncoder.encode("2174"))
                .email("admin@gmail.com")
                .roles(Set.of(Role.ADMIN))
                .accountCreatedTime(LocalDate.now())
                .lastAccountActivity(LocalDateTime.now())
                .build();
        User user = User.builder()
                .login("user")
                .password(passwordEncoder.encode("2174"))
                .email("user@gmail.com")
                .roles(Set.of(Role.USER))
                .accountCreatedTime(LocalDate.now())
                .lastAccountActivity(LocalDateTime.now())
                .target(Target.builder()
                        .gold(random.nextInt(1, 7))
                        .reputation(random.nextInt(1, 7))
                        .health(random.nextInt(1, 7))
                        .thirst(random.nextInt(1, 7))
                        .fight(random.nextInt(1, 7))
                        .shadow(random.nextInt(1, 7)).build())
                .build();
        if (!accountRepository.existsByEmail("admin@gmail.com"))
            accountRepository.save(admin);
        if (!accountRepository.existsByEmail("user@gmail.com"))
            accountRepository.save(user);

    }
}
