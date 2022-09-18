package com.execute.protocol.auth.controllers.api;
import com.example.protocol.dto.UserDto;
import com.execute.protocol.auth.configs.jwt.*;
import com.execute.protocol.auth.services.TokenService;
import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.entities.User;
import com.execute.protocol.core.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RestAuthController {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    @Autowired
    public RestAuthController(JwtProvider jwtProvider, UserDetailsService userDetailsService, TokenService tokenService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }


    @GetMapping("/loginByJwt")
    public AuthResponse loginByJwt(HttpServletRequest request){
        String token = tokenService.getToken();
        //String token = AuthSuccessHandler.getToken();
        String login = jwtProvider.getEmailFromToken(token);
        Account account = (Account) userDetailsService.loadUserByUsername(login);
        UserDto userDto = UserMapper.INSTANCE.mapUserToDto((User) account);

        return new AuthResponse(token, userDto);
    }
}
