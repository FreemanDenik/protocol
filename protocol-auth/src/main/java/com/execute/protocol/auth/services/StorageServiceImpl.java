package com.execute.protocol.auth.services;

import com.execute.protocol.auth.models.StgToken;
import com.execute.protocol.auth.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService{

    private final String HASH_KEY = "StgToken";

    private final TokenRepository tokenRepository;
   // private final RedisTemplate redisTemplate;

    public void addStorage(StgToken stgToken){
        tokenRepository.save(stgToken);
    }
    public String getRefreshToken(String email){

        return tokenRepository.findById(email).get().getRefreshToken();
    }
}
