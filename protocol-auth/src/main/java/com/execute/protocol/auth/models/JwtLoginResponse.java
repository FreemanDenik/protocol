package com.execute.protocol.auth.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtLoginResponse {
    protected final String type = "Bearer";
    protected String accessToken;
    protected String refreshToken;
    private String[] roles;
}
