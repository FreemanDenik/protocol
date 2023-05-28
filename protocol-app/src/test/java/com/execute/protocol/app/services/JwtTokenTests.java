package com.execute.protocol.app.services;

import com.execute.protocol.auth.exeptions.AuthException;
import com.execute.protocol.auth.models.*;
import com.execute.protocol.auth.services.AuthService;
import com.execute.protocol.core.entities.account.Account;
import com.execute.protocol.core.entities.account.Role;
import com.execute.protocol.core.repositories.AccountRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Чтобы выполнить тест, необходимо иметь зависимость БД в памяти для тестирования, например (H2),
 * А так же встраиваемый сервер Embedded-Redis
 */
// Аннотация позволяет использовать аннотации BeforeAll и AfterAll в не статическом виде
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@SpringBootTest
// Аннотация подтягивает тестовые бд, для mysql jpa в данном случаи используется база H2,
// для redis используется тот же redis только создается отдельный сервер,
// заменяет
// @DataJpaTest
// @DataRedisTest
@AutoConfigureTestDatabase
// Динамично добавляет property в тесты
// именно эта property add.redis.server.for.jwt.tests включает тестовый redis server
public class JwtTokenTests {
    private final String TEST_NAME = "test";
    private final String TEST_EMAIL = "test@email.ru";
    private final String TEST_PASSWORD = "password";
    private final JwtRequest TEST_USER = JwtRequest.builder().email(TEST_EMAIL).password(TEST_PASSWORD).build();
    private final JwtRequest INVALID_USER = JwtRequest.builder().email("invalid.email@mail.ru").password("invalid_password").build();
    private final String TYPE_TOKEN = "Bearer";
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;
    @AfterAll
    public void afterAll() {
        // Удаляем все аккаунты
        accountRepository.deleteAll();
    }
    @BeforeAll
    public void beforeAll() {
        // Сохраняем тестового пользователя в тестовую базу mysql (в H2)
        accountRepository.save(Account.builder()
                .login(TEST_NAME)
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .roles(Set.of(Role.USER))
                .accountCreatedTime(LocalDate.now())
                .lastAccountActivity(LocalDateTime.now())
                .build());
    }

    @Test
    public void JwtTest() throws InterruptedException {
        // Перед каждой генерации токена усыпляем тест на 1 секунду
        // Если это не сделать токены остаются одинаковыми
        // т.е. новых токенов не генерируется
        // Подозреваю, что это связанно с генерацией новых чисел, алгоритм которого отталкивается от времени,
        // а именно от секунд

        // Проверка на авторизацию заведомо не существующего пользователя
        assertThrows(AuthException.class, ()->authService.email(INVALID_USER));

        // Проверка существует ли тестовый пользователь в тестовой базе
        assertEquals(accountRepository.findByEmail(TEST_EMAIL).get().getLogin(), TEST_NAME);

        // Авторизация тестового пользователя
        JwtLoginResponse loginResponse = authService.email(TEST_USER);
        // Поле access токен не должно быть пустым
        assertNotNull(loginResponse.getAccessToken());
        // Поле refresh токен не должно быть пустым
        assertNotNull(loginResponse.getRefreshToken());
        // Тестовый пользователь должен иметь роль USER
        assertEquals(loginResponse.getRoles()[0], "USER");
        assertEquals(loginResponse.getType(), TYPE_TOKEN);


        // Усыпляем для корректной генерации токена
        Thread.sleep(1000);
        // Тестовый пользователь получает новый access токен
        JwtResponse refreshAssertToken = authService.getAccessToken(loginResponse.getRefreshToken());
        // Предыдущий и текущий access токены не должны совпадать
        assertNotEquals(refreshAssertToken.getAccessToken(), loginResponse.getAccessToken());
        // Refresh токена нету NULL
        assertNull(refreshAssertToken.getRefreshToken());
        assertEquals(refreshAssertToken.getType(), TYPE_TOKEN);

        // Усыпляем для корректной генерации токена
        Thread.sleep(1000);
        // Тестовый пользователь обновляет все токены
        JwtResponse refreshRefreshToken = authService.refresh(loginResponse.getRefreshToken());
        // Получить новый токен используя старый refresh токена не должно быть выполнимо
        assertThrows(AuthException.class, () -> authService.getAccessToken(loginResponse.getRefreshToken()));
        // Предыдущий и новый access токены не должны совпадать
        assertNotEquals(refreshRefreshToken.getAccessToken(), refreshAssertToken.getAccessToken());
        // Предыдущий и новый refresh токены не должны совпадать
        assertNotEquals(refreshRefreshToken.getRefreshToken(), loginResponse.getRefreshToken());
        assertEquals(refreshAssertToken.getType(), TYPE_TOKEN);

    }
}

//@TestPropertySource(properties = {
//        // Переопределяем строку "spring.redis.port" в property,
//        // это порт тестового встраиваемого сервера Redis, (взят от балды)
//        // Создается перед тестом и удаляется после
//        "spring.redis.port=7776"})