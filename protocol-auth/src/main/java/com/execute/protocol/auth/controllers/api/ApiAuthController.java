package com.execute.protocol.auth.controllers.api;

import com.execute.protocol.auth.configs.jwt.*;
import com.execute.protocol.auth.services.TokenService;
import com.execute.protocol.core.entities.acc.Account;
import com.execute.protocol.core.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ApiAuthController {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final AccountRepository accountRepository;
    private final TokenService tokenService;

    @Autowired
    public ApiAuthController(JwtProvider jwtProvider, UserDetailsService userDetailsService, AccountRepository accountRepository, TokenService tokenService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
    }

    // ЭТОТ КОНТРОЛЛЕР ПОКА НИГДЕ НЕ ИСПОЛЬЗУЕТСЯ!!!
    @GetMapping("/loginByJwt")
    public void loginByJwt(HttpServletResponse response) throws IOException {
        String token = tokenService.getToken();
        //String token = AuthSuccessHandler.getToken();
        String email = jwtProvider.getEmailFromToken(token);
        Account account = accountRepository.findByEmail(email);
       // UserDto userDto = UserMapper.INSTANCE.mapUserToDto((User) account);

        response.setHeader("Authorization", "Bearer " + token);
        response.sendRedirect("/game");


    }
}
