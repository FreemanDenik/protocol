package com.execute.protocol.auth.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtRefreshRequest {
    @NotBlank(message = "поле email не может быть пустым")
    @NotEmpty(message = "поле email не может быть пустым")
    public String refreshToken;
}
