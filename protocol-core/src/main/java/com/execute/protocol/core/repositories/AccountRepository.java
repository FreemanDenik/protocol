package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.acc.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Если операция DDL то используем @Modifying и @Transactional
    @Modifying
    @Transactional
    @Query(value = "UPDATE accounts as a SET a.string_id = a.id where a.id = :Id", nativeQuery = true)
    void setIdInStringId(@Param("Id") long id);
    Optional<Account> findAccountByEmail(String email);
    Optional<Account> findAccountById(long email);
    Optional<Account> findAccountByStringId(String stringId);

}
