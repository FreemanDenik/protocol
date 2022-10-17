package com.execute.protocol.auth.services;

import com.execute.protocol.core.entities.account.Account;
import com.execute.protocol.core.repositories.AccountRepository;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public Optional<Account> getAccountByEmail(@NotNull String email) {
        return Optional.ofNullable(accountRepository.findByEmail(email));
    }
    public Optional<Account> getAccountById(@NotNull long id) {
        return accountRepository.findAccountById(id);
    }

}
