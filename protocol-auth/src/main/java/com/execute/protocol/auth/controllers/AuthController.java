package com.execute.protocol.auth.controllers;

import com.execute.protocol.auth.configs.jwt.JwtProvider;
import com.execute.protocol.auth.enums.EnumCookie;
import com.execute.protocol.auth.services.TokenService;
import com.execute.protocol.auth.services.UserDetailsServiceImpl;
import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.repositories.AccountRepository;
import com.execute.protocol.core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
//@RequestMapping("/auth/")
public class AuthController {

    private final JwtProvider jwtProvider;
    public final UserRepository userRepository;
    public final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AuthController(JwtProvider jwtProvider, UserRepository userRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {

        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/login")
    public String loginGet(){

        return "login";
    }
    @PostMapping("/login")
    public String loginPost(String email, String password){
        Account account = accountRepository.findByEmail(email);
        if (Objects.nonNull(account)) {
            if (passwordEncoder.matches(password, account.getPassword())) {
                jwtProvider.generateToken(email, EnumCookie.SET_COOKIE);
            }
        }
        return "redirect:/admin";
    }
}
