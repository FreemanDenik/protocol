package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.redis.StgToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<StgToken, String> {

}
