package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.acc.Account;
import com.execute.protocol.core.entities.acc.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
}
