package com.execute.protocol.app.auth;

import com.execute.protocol.auth.models.JwtLoginResponse;
import com.execute.protocol.auth.models.JwtRequest;
import com.execute.protocol.auth.models.JwtResponse;
import com.execute.protocol.auth.models.StgToken;
import com.execute.protocol.auth.repositories.TokenRepository;
import com.execute.protocol.auth.services.*;
import com.execute.protocol.core.entities.acc.Account;
import com.execute.protocol.core.entities.acc.Role;
import com.execute.protocol.core.repositories.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

// Аннотация позволяет использовать аннотации BeforeAll и AfterAll в не статическом виде
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DataJpaTest
//@DataRedisTest

@SpringBootTest
@AutoConfigureTestDatabase
// Динамично добавляет пропертя в тесты
// именно эта пропертя add.redis.server.for.jwt.tests включает тестовый redis server
@TestPropertySource(properties = {"add.redis.server.for.jwt.tests=true"})
public class JwtTests {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String TEST_NAME = "test";
    private final String TEST_EMAIL = "test@email.ru";
    private final String TEST_PASSWORD = "password";
    @Mock
    private RestTemplate restTemplate;
    @Autowired
    private AuthService authService;

    @BeforeAll
    public void init() {
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
    public void JwtTest() throws URISyntaxException {


        assertEquals(accountRepository.findByEmail(TEST_EMAIL).getLogin(), TEST_NAME);

        JwtRequest jwtRequest = JwtRequest.builder().email(TEST_EMAIL).password(TEST_PASSWORD).build();
        JwtLoginResponse jwtLoginResponse = authService.email(jwtRequest);
        Mockito.when(restTemplate.getForEntity("http://localhost:8080/api/auth/login", JwtLoginResponse.class))
                .thenReturn(new ResponseEntity(jwtLoginResponse, HttpStatus.OK));


        var tt = restTemplate.getForEntity("http://localhost:8080/api/auth/login", JwtLoginResponse.class);
    }
}
