package com.execute.protocol.auth.controllers;


import com.execute.protocol.auth.dto.*;
import com.execute.protocol.auth.exeptions.AuthException;
import com.execute.protocol.auth.services.AuthService;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.acc.Role;
import com.execute.protocol.core.entities.acc.User;
import com.execute.protocol.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


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
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @PostMapping("register")
    public ResponseEntity register(@RequestBody JwtRegister jwtRegister) {

        if (!userRepository.existsByEmail(jwtRegister.getEmail())) {
            Random random = new Random();


            String name = nameArray[random.nextInt(nameArray.length)];
            String password = String.valueOf(random.nextInt(Integer.MAX_VALUE));
            String email = jwtRegister.getEmail();


            JwtRequest jwtRequest = JwtRequest.builder().email(email).password(password).build();
            User user = User.builder()
                    .login(name)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .roles(Set.of(Role.USER))
                    .target(Target.builder().money(random.nextInt(1, 7)).pollution(random.nextInt(1, 7)).build())
                    .build();
            userRepository.save(user);
            userRepository.setIdInStringId(user.getId());
            return this.login(jwtRequest);
        }else {

            return ResponseEntity.status(HttpStatus.OK).body("{\"user_already_exists\":\"true\"}");
        }
    }
    @PostMapping("login")
    public ResponseEntity login(@RequestBody JwtRequest authRequest) {
        if (!hasText(authRequest.getEmail()) || !hasText(authRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пустые данные");
        }

        try {
            final JwtResponse token = authService.email(authRequest);
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Не верный email или пароль");
        }
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody JwtRefreshRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody JwtRefreshRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}