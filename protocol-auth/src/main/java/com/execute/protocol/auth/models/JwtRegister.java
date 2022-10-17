package com.execute.protocol.auth.models;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRegister {
    @Email
    @NotBlank
    private String email;
}
