package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AccountRepository {
    User findByEmail(String email);
}
