package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.entities.AccountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, AccountId> {
    Account findByEmail(String email);
    boolean existsByEmail(String email);
    Account findByUsername(String username);
}
