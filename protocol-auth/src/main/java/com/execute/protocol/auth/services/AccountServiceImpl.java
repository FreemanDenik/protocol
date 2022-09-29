package com.execute.protocol.auth.services;

import com.execute.protocol.core.entities.acc.Account;
import com.execute.protocol.core.repositories.AccountRepository;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public Optional<Account> getByEmail(@NotNull String email) {
        return accountRepository.findAccountByEmail(email);
    }
    public Optional<Account> getById(@NotNull long id) {
        return accountRepository.findById(id);
    }
    public Optional<Account> getByStringId(@NotNull String id) {
        return accountRepository.findByStringId(id);
    }
}
