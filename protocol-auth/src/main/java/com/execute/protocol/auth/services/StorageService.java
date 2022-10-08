package com.execute.protocol.auth.services;


import com.execute.protocol.auth.models.StgToken;

public interface StorageService {
    void addStorage(StgToken stgToken);
    String getRefreshToken(String email);
}
