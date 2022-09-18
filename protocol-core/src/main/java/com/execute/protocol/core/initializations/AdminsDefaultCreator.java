package com.execute.protocol.core.initializations;

import com.execute.protocol.core.entities.Admin;
import com.execute.protocol.core.entities.Role;
import com.execute.protocol.core.enums.EnumProviders;
import com.execute.protocol.core.repositories.AccountRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Transactional
public class AdminsDefaultCreator {
    private final AccountRepository accountRepository;

    @Autowired
    public AdminsDefaultCreator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Bean("AdminsDefaultCreator")
    public void adminsDefaultCreator() {
        accountRepository.save(Admin.builder()
                .firstName("Admin")
                .lastName("DefaultAdmin")
                .username("admin")
                .password(BCrypt.hashpw("2174", BCrypt.gensalt(10)))
                .accountCreatedTime(LocalDate.now())
                .lastAccountActivity(LocalDateTime.now())
                .email("denikvy@gmail.com")
                .provider(EnumProviders.PROTOCOL)
                .role(Role.ROLE_ADMIN)
                .build());
    }
}
