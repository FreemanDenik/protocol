package com.execute.protocol.auth.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    protected final String type = "Bearer";
    protected String accessToken;
    protected String refreshToken;
}
