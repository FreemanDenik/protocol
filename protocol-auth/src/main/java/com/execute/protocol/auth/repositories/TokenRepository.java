package com.execute.protocol.auth.repositories;

import com.execute.protocol.auth.models.StgToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<StgToken, String> {

}
