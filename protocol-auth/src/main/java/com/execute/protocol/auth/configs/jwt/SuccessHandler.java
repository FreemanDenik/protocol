package com.execute.protocol.auth.configs.jwt;


import com.execute.protocol.auth.models.OtherOAuth2User;
import com.execute.protocol.auth.services.UserDetailsImpl;
import com.execute.protocol.core.converters.JavaDateConverter;
import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.entities.Role;
import com.execute.protocol.core.entities.User;
import com.execute.protocol.core.repositories.AccountRepository;
import com.execute.protocol.core.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {
    private final UserDetailsImpl userService;
    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private static String token;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    public SuccessHandler(JwtProvider jwtProvider, UserDetailsImpl userService, AccountRepository accountRepository, UserRepository userRepository) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        String email = ((OtherOAuth2User) authentication.getPrincipal()).getEmail();
        Account account = null;

        if (userService.existsByEmail(email)) {
            try {
                account = (Account) userService.loadUserByUsername(email);

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                                SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                                Collections.singleton(account.getRole())));
            } catch (UsernameNotFoundException e) {
                httpServletResponse.sendRedirect("/oauth2reg");
                return;
            }
        } else {
            OtherOAuth2User customOAuth2User = (OtherOAuth2User) authentication.getPrincipal();
            account = accountRepository.findByEmail(email);
            if (account == null) {
                User user = new User();
                user.setRole(Role.ROLE_USER);
                user.setAccountCreatedTime(LocalDate.now());
                user.setLastAccountActivity(LocalDateTime.now());
                user.setEmail(customOAuth2User.getEmail());
                user.setFirstName(customOAuth2User.getName());
                user.setLastName(customOAuth2User.getLastName());
                //user.setCity(customOAuth2User.getCity());
                //user.setPassword("sdsdasdasd");
                user.setUsername(customOAuth2User.getName());
                user.setBirthday(JavaDateConverter.parserToLocalDate(customOAuth2User.getBirthday()));
                userRepository.save(user);

                account = (Account) userService.loadUserByUsername(user.getEmail());
            }
        }
        token = jwtProvider.generateToken(account.getEmail());
        log.debug("Успешная авторизация id: {},  email: {},  JWT: {}", account.getId(), account.getEmail(), token);

        httpServletResponse.sendRedirect("/loginByJwt");
    }

    public static String getToken() {
        return token;
    }

}

