package com.execute.protocol.app.controllers;

import com.execute.protocol.app.InitializationTests;
import com.execute.protocol.app.TestTemplateLogin;
import com.execute.protocol.auth.models.JwtLoginResponse;
import com.execute.protocol.auth.models.JwtRequest;
import com.execute.protocol.auth.models.JwtResponse;
import com.execute.protocol.auth.services.AccountService;
import com.execute.protocol.auth.services.AuthService;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.entities.account.Account;
import com.execute.protocol.core.entities.account.Role;
import com.execute.protocol.core.mappers.EventMapper;
import com.execute.protocol.core.repositories.AccountRepository;
import com.execute.protocol.core.repositories.EventRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// параметры (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) нужны для TestRestTemplate,
// чтобы использовать относительный путь, типа /controller/action/{param}

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        // этот профиль исползуется, чтобы отключать бины, а точнее бин инициализации
        //"spring.profiles.active=test",
        // Определяя этот атрибут, мы говорим, что спящий режим инициализирует ленивое
        // состояние даже для внешних транзакций. Это создаст новый временный сеанс
        "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"})
//@Import(ConfigTests.class)
public class GameControllerTests {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;
    private final String TEST_EMAIL = "test@gmail.com";
    private final String TEST_PASSWORD = "password";
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private TestTemplateLogin templateLogin;
    @AfterAll
    public void afterAll() {
        // Удаляем все аккаунты
        accountRepository.deleteAll();
    }
    @BeforeAll
    public void beforeAll() {
        accountRepository.save(Account.builder()
                .login("test_name")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .roles(Set.of(Role.USER))
                .accountCreatedTime(LocalDate.now())
                .lastAccountActivity(LocalDateTime.now())
                .build());
        eventRepository.saveAll(Set.of(
                Event.builder().eventText("event 1 text").build()
        ));
       templateLogin = new TestTemplateLogin(testRestTemplate, TEST_EMAIL, TEST_PASSWORD);
    }
    @Test
    public void e(){
       ResponseEntity<JwtLoginResponse> result =
               templateLogin.post("/api/game/initializer", new HttpEntity<>(new HttpHeaders()), "", JwtLoginResponse.class);
    }
}
