package com.execute.protocol.auth.configs.jwt;


import com.execute.protocol.auth.enums.EnumCookie;
import com.execute.protocol.auth.models.OtherOAuth2User;
import com.execute.protocol.core.converters.JavaDateConverter;
import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.entities.Role;
import com.execute.protocol.core.entities.User;
import com.execute.protocol.core.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

/**
 * Handler успешной авторизации/аутентификации
 */
@Slf4j
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    public AuthSuccessHandler(
            JwtProvider jwtProvider,
            UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {

        OtherOAuth2User otherOAuth2User = (OtherOAuth2User) authentication.getPrincipal();
        // Если атрибут mail пуст формируем уникальную строку из имени провайдера и id клиента в этом сервисе
        // Уникальная строка в поле mail формируется потому что, из mail'а формируется токен и по mail'у, происходит поиск пользователя
        String email =
                otherOAuth2User.getEmail() != null ?
                otherOAuth2User.getEmail() :
                otherOAuth2User.getProviderName() + "." + otherOAuth2User.getClientId();

        if (userRepository.existsByEmail(email)) {
            try {
                Account account = userRepository.findByEmail(email);

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                                SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                                Collections.singleton(account.getRole())));
                log.info("Пользователь {} прошел авторизацию", email );
            } catch (UsernameNotFoundException e) {
                // В случаи ошибки при авторизации пользователя перенаправляем пользователя сюда (надо допилить)
                log.warn("Пользователь {} не прошел авторизацию и перенаправляется на стандартную страницу регистрации", email );
                httpServletResponse.sendRedirect("/oauth2reg");

            }
        } else {
            Account account = userRepository.findByEmail(email);
            if (account == null) {

                User user = User.builder()
                        .firstName(otherOAuth2User.getFirstName())
                        .lastName(otherOAuth2User.getLastName())
                        .username(otherOAuth2User.getName())
                        .role(Role.ROLE_USER)
                        .clientId(otherOAuth2User.getClientId())
                        .provider(otherOAuth2User.getProviderName())
                        .email(email)
                        .birthday(JavaDateConverter.parserToLocalDate(otherOAuth2User.getBirthday()))
                        .accountCreatedTime(LocalDate.now())
                        .lastAccountActivity(LocalDateTime.now())
                        .build();

                userRepository.save(user);
            }
        }
        // Создание и помещение в куки токена
        jwtProvider.generateToken(email, EnumCookie.SET_COOKIE);

        //token = jwtProvider.generateToken(account.getEmail());
        //log.debug("Успешная авторизация id: {},  email: {},  JWT: {}", account.getId(), account.getEmail(), token);

        //httpServletResponse.sendRedirect("/loginByJwt");
        httpServletResponse.sendRedirect("/enter");
    }


}

