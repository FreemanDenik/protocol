package com.execute.protocol.core.initializations;

import com.execute.protocol.core.entities.acc.Admin;
import com.execute.protocol.core.entities.acc.Role;
import com.execute.protocol.core.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class AdminsDefaultCreator {
    private final AdminRepository  adminRepository;
    private final PasswordEncoder passwordEncoder;
    @Bean("AdminsDefaultCreator")
    public void adminsDefaultCreator() {
        adminRepository.save(Admin.builder()
                //.firstName("Admin")
                //.lastName("DefaultAdmin")
                .login("admin")
                .password(passwordEncoder.encode("2174"))
                //.accountCreatedTime(LocalDate.now())
                //.lastAccountActivity(LocalDateTime.now())
                .email("denikvy@gmail.com")
                // .provider(EnumProviders.PROTOCOL)
                .roles(Set.of(Role.ADMIN))
                .build());
    }
}
