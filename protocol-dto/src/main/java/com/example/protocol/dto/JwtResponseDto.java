package com.example.protocol.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {
    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
}
