package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.entities.AccountId;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AccountRepository {
    Account findAccountByAccountId(AccountId accountId);
    boolean existsAccountByAccountId(AccountId accountId);
}
