package com.execute.protocol.auth.models;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRegister {
    @Email(message = "не корръектный email адрес")
    @NotBlank(message = "поле email не может быть пустым")
    @NotEmpty(message = "поле email не может быть пустым")
    private String email;
}
