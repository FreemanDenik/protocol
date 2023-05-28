package com.execute.protocol.core.enums;

public enum EmErrors {
    MODEL_BIND(2000), // ошибка валидации модели (привязки данных)
    REGISTRATION(3000), // общая ошибка регистрации
    MODEL_VALIDATION(1000), // ошибки валидации данных (пример: такой email уже используется)
    LOGIN(4000), // общая ошибка входа
    LOGIN_ENTER(4001), // ошибка при входе
    ACCESS_TOKEN(5000),   // ошибка при получении токена
    REFRESH_TOKEN(5001), // ошибка при получении рефреш токена
    INITIALIZER(6000), // ошибка при инициализации игры
    GAME(7000), // общая ошибка в игре
    GAME_NULL(7001), // ошибка в игре при получении, нулевых значении (пример: не найдено событие по id, не найден ответ по id )
    GAME_OVER(9000); // статус игра окончена
    private int code;
    public int getCode(){
        return code;
    }
    EmErrors(int code) {
        this.code = code;
    }
}
