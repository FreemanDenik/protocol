package com.execute.protocol.auth.services;


import com.execute.protocol.core.entities.redis.StgToken;

public interface StorageService {
    void addStorage(StgToken stgToken);
    String getRefreshToken(String email);
}
