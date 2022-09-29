package com.execute.protocol.app.controllers;

import com.example.protocol.dto.*;
import com.execute.protocol.app.models.JwtRegister;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.acc.Role;
import com.execute.protocol.core.entities.acc.User;
import com.execute.protocol.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AccountController {


    final String[] nameArray = {
            "Яков",
            "Белин"
    };
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final RestTemplate restTemplate;

    @PostMapping("register")
    public ResponseEntity register(@RequestBody JwtRegister jwtRegister, @Value("${jwt.server.url}") String jwtServerUrl) {
        jwtServerUrl += "/api/auth/login";
        if (!userRepository.existsByEmail(jwtRegister.getEmail())) {
            Random random = new Random();


            String name = nameArray[random.nextInt(nameArray.length)];
            String password = String.valueOf(random.nextInt(Integer.MAX_VALUE));
            String email = jwtRegister.getEmail();


            JwtRequestDto jwtRequest = JwtRequestDto.builder().email(email).password(password).build();

            userRepository.save(
                    User.builder()
                            .login(name)
                            .password(passwordEncoder.encode(password))
                            .email(email)
                            .roles(Set.of(Role.USER))
                            .target(Target.builder().money(random.nextInt(1, 7)).pollution(random.nextInt(1, 7)).build())
                            .build());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<JwtRequestDto> request = new HttpEntity<>(jwtRequest, headers);
            return restTemplate.exchange(jwtServerUrl, HttpMethod.POST, request, JwtResponseDto.class);


        }else {

            return ResponseEntity.status(HttpStatus.OK).body("{\"user_already_exists\":\"true\"}");
        }
    }
}
