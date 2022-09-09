package com.execute.protocol.auth.configs.jwt;


import com.execute.protocol.auth.enums.EnumCookie;
import com.execute.protocol.auth.models.OtherOAuth2User;
import com.execute.protocol.auth.services.UserDetailsImpl;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

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
                                        Authentication authentication) throws IOException, ServletException {

        String email = ((OtherOAuth2User) authentication.getPrincipal()).getEmail();
        Account account = null;

        if (userRepository.existsByEmail(email)) {
            try {
                account = userRepository.findByEmail(email);

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                                SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                                Collections.singleton(account.getRole())));
            } catch (UsernameNotFoundException e) {
                httpServletResponse.sendRedirect("/oauth2reg");

            }
        } else {
            OtherOAuth2User customOAuth2User = (OtherOAuth2User) authentication.getPrincipal();
            account = userRepository.findByEmail(email);
            if (account == null) {

                User user = User.builder()
                        .firstName(customOAuth2User.getFirstName())
                        .lastName(customOAuth2User.getLastName())
                        .username(customOAuth2User.getName())
                        .role(Role.ROLE_USER)
                        .email(customOAuth2User.getEmail())
                        .birthday(JavaDateConverter.parserToLocalDate(customOAuth2User.getBirthday()))
                        .accountCreatedTime(LocalDate.now())
                        .lastAccountActivity(LocalDateTime.now())
                        .build();

                userRepository.save(user);
                account = user;
            }
        }
        // Создание помещение в куки токена
        String token = jwtProvider.generateToken(account.getEmail(), EnumCookie.SET_COOKIE);

        //token = jwtProvider.generateToken(account.getEmail());
        log.debug("Успешная авторизация id: {},  email: {},  JWT: {}", account.getId(), account.getEmail(), token);

        //httpServletResponse.sendRedirect("/loginByJwt");
        httpServletResponse.sendRedirect("/enter");
    }


}

