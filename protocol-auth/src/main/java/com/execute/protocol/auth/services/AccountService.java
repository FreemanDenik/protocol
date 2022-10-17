package com.execute.protocol.auth.services;

import com.execute.protocol.core.entities.account.Account;
import com.sun.istack.NotNull;

import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountById(@NotNull long id);
    Optional<Account> getAccountByEmail(@NotNull String email);
}
