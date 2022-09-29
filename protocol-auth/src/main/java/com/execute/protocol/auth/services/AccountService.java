package com.execute.protocol.auth.services;

import com.execute.protocol.core.entities.acc.Account;
import com.sun.istack.NotNull;

import java.util.Optional;

public interface AccountService {
    Optional<Account> getByEmail(@NotNull String login);
}
