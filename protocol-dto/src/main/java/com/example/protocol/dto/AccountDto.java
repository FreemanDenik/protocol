package com.example.protocol.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class AccountDto {
    /**
     * Первичный ключ
     */
    private Long id;

    /**
     * Имя
     */
    private String firstName;

    /**
     * Фамилия
     */
    private String lastName;

    /**
     * Отчество
     */
    private String middleName;

    /**
     * Логин
     */
    private String username;

    /**
     * Роль
     */
    private RoleDto role;

    /**
     * Электронная почта
     */
    private String email;
}
