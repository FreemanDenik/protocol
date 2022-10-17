package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.account.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AccountRepository {

    boolean existsByEmail(String email);

    User findByEmail(String email);
    @Query("FROM User WHERE email = :email AND currentEvent = :event")
    User findByEmailAndEvent(@Param("email") String email, @Param("event") long event);

}
