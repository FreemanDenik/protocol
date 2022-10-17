package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Если операция DDL то используем @Modifying и @Transactional
//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE accounts as a SET a.string_id = a.id where a.id = :Id", nativeQuery = true)
//    void setIdInStringId(@Param("Id") long id);

    Account findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Account> findAccountById(long email);



}
