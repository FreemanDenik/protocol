package com.execute.protocol.auth.models;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    @Email(message = "не корръектный email адрес")
    @NotBlank(message = "поле email не может быть пустым")
    @NotEmpty(message = "поле email не может быть пустым")

    private String email;
    @NotBlank(message = "поле password не может быть пустым" )
    @Length(message = "поле должно быть не меньше 6 символов", min = 6)
    private String password;

}