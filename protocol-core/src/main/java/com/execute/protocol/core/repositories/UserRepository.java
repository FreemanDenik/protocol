package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.acc.Account;
import com.execute.protocol.core.entities.acc.Admin;
import com.execute.protocol.core.entities.acc.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends AccountRepository {

    boolean existsByEmail(String email);


    User findByEmail(String email);
    @Query("FROM User WHERE email = :email AND currentEvent = :event")
    User findByEmailAndEvent(@Param("email") String email, @Param("event") long event);

}
