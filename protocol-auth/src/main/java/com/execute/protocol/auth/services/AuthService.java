package com.execute.protocol.auth.services;


import com.execute.protocol.auth.dto.*;
import com.execute.protocol.auth.exeptions.AuthException;
import lombok.NonNull;

public interface AuthService {
    JwtResponse email(@NonNull JwtRequest authRequest) throws AuthException;
    JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException;
    JwtResponse refresh(@NonNull String refreshToken) throws AuthException;
    JwtAuthentication getAuthInfo();
}
