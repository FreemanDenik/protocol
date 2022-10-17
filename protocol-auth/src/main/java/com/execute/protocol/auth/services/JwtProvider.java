package com.execute.protocol.auth.services;

import com.execute.protocol.core.entities.account.Account;
import io.jsonwebtoken.Claims;
import lombok.NonNull;

public interface JwtProvider {
    String generateAccessToken(@NonNull Account account);
    String generateRefreshToken(@NonNull Account account);
    boolean validateAccessToken(@NonNull String accessToken);
    boolean validateRefreshToken(@NonNull String refreshToken);
    Claims getAccessClaims(@NonNull String token);
    Claims getRefreshClaims(@NonNull String token);
}
