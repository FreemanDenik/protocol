package com.execute.protocol.auth.controllers;


import com.execute.protocol.auth.models.*;
import com.execute.protocol.auth.exeptions.AuthException;
import com.execute.protocol.auth.services.AuthService;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.acc.Role;
import com.execute.protocol.core.entities.acc.User;
import com.execute.protocol.core.repositories.AccountRepository;
import com.execute.protocol.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

import static org.springframework.util.StringUtils.hasText;
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    final String[] nameArray = {
            "Яков",
            "Белин"
    };
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final AccountRepository accountRepository;

    /**
     * Метод регистрации пользователя
     * @param jwtRegister
     * @return
     */
    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody JwtRegister jwtRegister, @Value("${server.port}") String serverPort) {

        if (!userRepository.existsByEmail(jwtRegister.getEmail())) {
            Random random = new Random();
            // Получаем случайное имя из готового списка
            String name = nameArray[random.nextInt(nameArray.length)];
            // Получаем случайный пароль
            String password = String.valueOf(random.nextInt(Integer.MAX_VALUE));
            String email = jwtRegister.getEmail();
            // Создаем и заполняем модель данных JwtRequest
            JwtRequest jwtRequest = JwtRequest.builder().email(email).password(password).build();
            // Создаем и заполняем модель данных User
            User user = User.builder()
                    .login(name)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .roles(Set.of(Role.USER))
                    .accountCreatedTime(LocalDate.now())
                    .lastAccountActivity(LocalDateTime.now())
                    .target(
                            Target.builder()
                                    .gold(random.nextInt(1, 7))
                                    .reputation(random.nextInt(1, 7))
                                    .health(random.nextInt(1, 7))
                                    .thirst(random.nextInt(1, 7))
                                    .fight(random.nextInt(1, 7))
                                    .shadow(random.nextInt(1, 7))
                                    .build())
                    .build();
            // Сохраняем user в базу
            accountRepository.save(user);
            // Создаем объект заголовков
            HttpHeaders headers = new HttpHeaders();
            // Указываем тип передаваемых данных JSON
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Заполняем request модель моделью jwtRequest и заголовками headers
            HttpEntity<JwtRequest> request = new HttpEntity<>(jwtRequest, headers);
            // Даем запрос на метод login
           return restTemplate.postForEntity("http://localhost:"+serverPort+"/api/auth/login", request, JwtResponse.class);

        }else {
            // Ответ "user_already_exists" дает фронту знать что пользователь уже есть
            return ResponseEntity.ok("{\"user_already_exists\":\"0\"}");
        }
    }
    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody JwtRequest authRequest) {

        try {
            // Формируем и получаем токены
            final JwtResponse token = authService.email(authRequest);
            // Возвращаем токены в качестве ответа
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Не верный email или пароль");
        }

    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@Valid @RequestBody JwtRefreshRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@Valid @RequestBody JwtRefreshRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}