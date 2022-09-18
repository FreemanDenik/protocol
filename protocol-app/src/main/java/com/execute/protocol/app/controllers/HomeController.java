package com.execute.protocol.app.controllers;

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
public class HomeController {

    private final UserDetailsServiceImpl userDetails;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    public final UserRepository userRepository;
    public final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public HomeController(UserDetailsServiceImpl userDetails, JwtProvider jwtProvider, UserDetailsService userDetailsService, UserRepository userRepository, AccountRepository accountRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userDetails = userDetails;
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/enter")
    public String enter(HttpServletResponse response, HttpServletRequest request, Model model){

        String email = jwtProvider.getEmailFromToken(tokenService.getToken());
        Account account = accountRepository.findByEmail(email);
        model.addAttribute("firstName", account.getFirstName());

        return "enter";
    }


    private Account getCurrentAccount() {
        String email;
        var tt = SecurityContextHolder.getContext().getAuthentication().toString();
        if (SecurityContextHolder.getContext().getAuthentication().toString().contains("given_name")) {
            email = ((DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = (Account) auth.getPrincipal();
            email = account.getEmail();
        }

        return (Account) userDetails.loadUserByUsername(email);
    }
}
