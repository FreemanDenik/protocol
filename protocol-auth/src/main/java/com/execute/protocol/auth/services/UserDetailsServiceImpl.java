package com.execute.protocol.auth.services;

import com.execute.protocol.core.entities.acc.Account;
import com.execute.protocol.core.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Поиск выполняется по email хоть и написано, что по логину
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        log.info("Account authorization: {}", email);
        if (account == null) {
            throw new UsernameNotFoundException("userDetailsAccount is null, not found ");
        } else {
            account.setLastAccountActivity(LocalDateTime.now());
            return account;
        }
    }

}
