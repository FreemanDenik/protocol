package com.execute.protocol.auth.controllers;


import com.execute.protocol.auth.models.*;
import com.execute.protocol.auth.exeptions.AuthException;
import com.execute.protocol.auth.services.AuthService;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.account.Role;
import com.execute.protocol.core.entities.account.User;
import com.execute.protocol.core.helpers.JsonAnswer;
import com.execute.protocol.core.repositories.AccountRepository;
import com.execute.protocol.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Log4j2
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
     * Регистрации пользователя
     *
     * @param jwtRegister передаваемые данные
     * @param serverPort порт сервеа
     * @param bindResult валидация привязки данныех
     * @param jsonAnswer список ошибок (если таковы есть)
     * @return {@link JwtLoginResponse}
     */
    @PostMapping(value = "register")
    public ResponseEntity<? super JwtLoginResponse> register(
            @Valid @RequestBody JwtRegister jwtRegister,
            @Value("${server.port}") String serverPort,
            BindingResult bindResult,
            JsonAnswer jsonAnswer) {
        if (bindResult.hasErrors()) {
            log.warn("Ошибка при регистрации (валидация регистрационных данных): " + bindResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
            jsonAnswer.addMessage(bindResult.getAllErrors());
        } else if (!userRepository.existsByEmail(jwtRegister.getEmail())) {
            try {
                Random random = new Random();
                // Получаем случайное имя из готового списка
                String name = nameArray[random.nextInt(nameArray.length)];
                // Получаем случайный пароль
                String password = String.valueOf(random.nextInt(Integer.MAX_VALUE));
                String email = jwtRegister.getEmail();
                // Создаем и заполняем модель данных JwtRequest
                JwtRequest jwtRequest = JwtRequest.builder().email(email).password(password).build();
                // Создаем и заполняем модель данных User
                Target target = Target.builder()
                        .gold((byte) random.nextInt(15, 25))
                        .reputation((byte) random.nextInt(15, 25))
                        .influence((byte) random.nextInt(15, 25))
                        .shadow((byte) random.nextInt(15, 25))
                        .luck((byte) random.nextInt(15, 25)).build();
                log.info("созданные параметры при регестрации " + jwtRegister.getEmail() + ", параметры " + target.toString());
                User user = User.builder()
                        .login(name)
                        .password(passwordEncoder.encode(password))
                        .email(email)
                        .roles(Set.of(Role.USER))
                        .accountCreatedTime(LocalDate.now())
                        .lastAccountActivity(LocalDateTime.now())
                        .target(target)
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
                log.info("регистрация " + jwtRegister.getEmail() + " прошла успешно");
                return restTemplate.postForEntity("http://localhost:" + serverPort + "/api/auth/login", request, JwtLoginResponse.class);
            } catch (Exception e) {
                log.warn("произошла ошибка при регестрайии: " + e.getMessage());
                jsonAnswer.addMessage("произошла ошибка при регестрайии");
            }
        } else {
            jsonAnswer.addMessage("такой email уже используется");
            log.warn("такой email уже используется: " + jwtRegister.getEmail());
        }
        return ResponseEntity.ok(jsonAnswer);
    }
    /**
     * Логин/Вход
     * @param authRequest передаваемые данные
     * @param bindResult валидация привязки данныех
     * @param jsonAnswer список ошибок (если таковы есть)
     * @return {@link JwtLoginResponse}
     */
    @PostMapping(value = "login")
    public ResponseEntity<? super JwtLoginResponse> login(
            @Valid @RequestBody JwtRequest authRequest,
            BindingResult bindResult,
            JsonAnswer jsonAnswer) {
        // Проверяем валидацию переданных значении
        if (!bindResult.hasErrors()) {
            try {
                // Формируем и получаем токены
                final JwtLoginResponse token = authService.email(authRequest);
                log.info("Вход произведен успешно");
                // Возвращаем токены в качестве ответа
                return ResponseEntity.ok(token);
            } catch (AuthException e) {
                log.warn("Ошибка при входе: " + e.getMessage());
                jsonAnswer.addMessage(e.getMessage());
            }
        } else {
            log.warn("Ошибка при входе (валидация входных данных): " + bindResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
            jsonAnswer.addMessage(bindResult.getAllErrors());
        }
        return ResponseEntity.ok(jsonAnswer);
    }
    /**
     * Обновление токена с помошью рефреш токена
     * @param request передаваемые данные
     * @param bindResult валидация привязки данныех
     * @param jsonAnswer список ошибок (если таковы есть)
     * @return {@link JwtResponse}
     */
    @PostMapping("token")
    public ResponseEntity<? super JwtResponse> getNewAccessToken(
            @Valid @RequestBody JwtRefreshRequest request,
            BindingResult bindResult,
            JsonAnswer jsonAnswer) {
        if (bindResult.hasErrors()) {
            log.warn("Ошибка валидации при полечении токена: " + bindResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
            jsonAnswer.addMessage(bindResult.getAllErrors());
        } else {
            try {
                final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
                return ResponseEntity.ok(token);
            }catch (Exception e){
                log.warn("Ошибка при получении токена: " + e.getMessage());
                jsonAnswer.addMessage("Ошибка при получении токена");
            }
        }
        return ResponseEntity.ok(jsonAnswer);
    }

    /**
     * Обновление рефреш токена
     * @param request передаваемые данные
     * @param bindResult валидация привязки данныех
     * @param jsonAnswer список ошибок (если таковы есть)
     * @return {@link JwtResponse}
     */
    @PostMapping("refresh")
    public ResponseEntity<? super JwtResponse> getNewRefreshToken(
            @Valid @RequestBody JwtRefreshRequest request,
            BindingResult bindResult,
            JsonAnswer jsonAnswer) {
        if (bindResult.hasErrors()) {
            log.warn("Ошибка валидации при обновлении рефреш токена: " + bindResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
            jsonAnswer.addMessage(bindResult.getAllErrors());
        } else{
            try {
                final JwtResponse token = authService.refresh(request.getRefreshToken());
                return ResponseEntity.ok(token);
            }catch (Exception e){
                log.warn("Ошибка при обновлении рефреш токена: " + e.getMessage());
                jsonAnswer.addMessage("Ошибка при обновлении рефреш токена");
            }
        }
        return ResponseEntity.ok(jsonAnswer);
    }

}