package com.execute.protocol.auth.controllers;

import com.execute.protocol.auth.configs.jwt.JwtProvider;
import com.execute.protocol.auth.enums.EnumCookie;
import com.execute.protocol.core.entities.acc.Account;
import com.execute.protocol.core.entities.acc.Role;
import com.execute.protocol.core.repositories.AccountRepository;
import com.execute.protocol.core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    @GetMapping("/register")
    public String registerGet() {
        return "auth/register";
    }
    @PostMapping("/register")
    public String registerPost() {
        return "redirect:/";
    }
    /**
     * GET Метод контроллера классический вход на сайт
     * @return
     */
    @GetMapping("/login")
    public String loginGet() {
        return "auth/login";
    }
    /**
     *  POST Метод контроллера классический вход на сайт
     * @return
     */
    @PostMapping("/login")
    public String loginPost(String email, String password) {
        Account account = accountRepository.findByEmail(email);
        if (Objects.nonNull(account)) {
            if (passwordEncoder.matches(password, account.getPassword())) {
                jwtProvider.generateToken(email, EnumCookie.SET_COOKIE);
                if (account.getRole() == Role.ROLE_ADMIN) {
                    return "redirect:/admin/";
                } else if (account.getRole() == Role.ROLE_USER) {
                    return "redirect:/game";
                }
            }
        }
        return "redirect:/";
    }
}
