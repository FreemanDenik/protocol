package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.acc.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
    boolean existsByEmail(String email);
    Account findByUsername(String username);
}
