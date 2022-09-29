package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.acc.Account;
import com.execute.protocol.core.entities.acc.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByEmail(String email);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
