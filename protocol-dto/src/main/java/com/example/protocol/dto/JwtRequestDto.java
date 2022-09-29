package com.example.protocol.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestDto {
    private String email;
    private String password;

}