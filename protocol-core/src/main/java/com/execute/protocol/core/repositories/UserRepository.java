package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.acc.AuthUser;
import com.execute.protocol.core.entities.acc.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends AuthUserRepository {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
