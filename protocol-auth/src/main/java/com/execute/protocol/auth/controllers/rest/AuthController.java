package com.execute.protocol.auth.controllers.rest;


import com.example.protocol.dto.UserDto;
import com.execute.protocol.auth.configs.jwt.*;
import com.execute.protocol.auth.services.UserDetailsImpl;
import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.entities.User;
import com.execute.protocol.core.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    @Autowired
    public AuthController(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("/loginByJwt")
    public AuthResponse loginByJwt(){
        String token = SuccessHandler.getToken();
        String login = jwtProvider.getLoginFromToken(token);
        Account account = (Account) userDetailsService.loadUserByUsername(login);
        UserDto userDto = UserMapper.INSTANCE.mapUserToDto((User) account);

        return new AuthResponse(token, userDto);
    }
}
