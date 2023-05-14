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
                .password(passwordEncoder.encode("2174777"))
                .email("admin@gmail.com")
                .roles(Set.of(Role.ADMIN))
                .accountCreatedTime(LocalDate.now())
                .lastAccountActivity(LocalDateTime.now())
                .build();
        User user = User.builder()
                .login("user")
                .password(passwordEncoder.encode("2174777"))
                .email("user@gmail.com")
                .roles(Set.of(Role.USER))
                .accountCreatedTime(LocalDate.now())
                .lastAccountActivity(LocalDateTime.now())
                .target(Target.builder()
                        .gold((byte) random.nextInt(15, 25))
                        .reputation((byte)random.nextInt(15, 25))
                        .influence((byte)random.nextInt(15, 25))
                        .shadow((byte)random.nextInt(15, 25))
                        .luck((byte)random.nextInt(15, 25)).build())
                .build();
        User user2 = User.builder()
                .login("user2")
                .password(passwordEncoder.encode("2174777"))
                .email("user2@gmail.com")
                .roles(Set.of(Role.USER))
                .accountCreatedTime(LocalDate.now())
                .lastAccountActivity(LocalDateTime.now())
                .target(Target.builder()
                        .gold((byte) random.nextInt(15, 25))
                        .reputation((byte)random.nextInt(15, 25))
                        .influence((byte)random.nextInt(15, 25))
                        .shadow((byte)random.nextInt(15, 25))
                        .luck((byte)random.nextInt(15, 25)).build())
                .build();
        if (!accountRepository.existsByEmail("admin@gmail.com"))
            accountRepository.save(admin);
        if (!accountRepository.existsByEmail("user@gmail.com"))
            accountRepository.save(user);
        if (!accountRepository.existsByEmail("user2@gmail.com"))
            accountRepository.save(user2);

    }
}
