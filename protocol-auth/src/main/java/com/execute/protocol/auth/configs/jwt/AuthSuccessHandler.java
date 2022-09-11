package com.execute.protocol.auth.configs.jwt;


import com.execute.protocol.auth.enums.EnumCookie;
import com.execute.protocol.core.entities.AccountId;
import com.execute.protocol.core.enums.EnumProviders;
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
        // формируем составной ключ
        AccountId accountId = new AccountId(otherOAuth2User.getClientId(), otherOAuth2User.getProviderName());
        Account account = null;

        if (userRepository.existsAccountByAccountId(accountId)) {
            try {
                account = userRepository.findAccountByAccountId(accountId);

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                                SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                                Collections.singleton(account.getRole())));
            } catch (UsernameNotFoundException e) {
                httpServletResponse.sendRedirect("/oauth2reg");

            }
        } else {
            account = userRepository.findAccountByAccountId(accountId);
            if (account == null) {

                User user = User.builder()
                        .accountId(accountId)
                        .firstName(otherOAuth2User.getFirstName())
                        .lastName(otherOAuth2User.getLastName())
                        .username(otherOAuth2User.getName())
                        .role(Role.ROLE_USER)
                        .email(otherOAuth2User.getEmail())
                        .birthday(JavaDateConverter.parserToLocalDate(otherOAuth2User.getBirthday()))
                        .accountCreatedTime(LocalDate.now())
                        .lastAccountActivity(LocalDateTime.now())
                        .build();

                userRepository.save(user);
                account = user;
            }
        }
        // Создание помещение в куки токена
        //jwtProvider.generateToken(account.getEmail(), EnumCookie.SET_COOKIE);
        jwtProvider.generateToken(accountId.toString(), EnumCookie.SET_COOKIE);

        //token = jwtProvider.generateToken(account.getEmail());
        //log.debug("Успешная авторизация id: {},  email: {},  JWT: {}", account.getId(), account.getEmail(), token);

        //httpServletResponse.sendRedirect("/loginByJwt");
        httpServletResponse.sendRedirect("/enter");
    }


}

