package com.execute.protocol.auth.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class JwtRegister {
    @Email
    @NotBlank
    private String email;
}
