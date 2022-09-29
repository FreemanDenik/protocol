package com.example.protocol.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtRefreshRequestDto {
    public String refreshToken;
}
