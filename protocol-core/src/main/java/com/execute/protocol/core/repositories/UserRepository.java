package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.acc.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends AccountRepository {
    User findUserByStringId(String stringId);
    boolean existsByEmail(String email);

}
