package com.execute.protocol.app.controllers;

import com.example.protocol.dto.UserDto;
import com.execute.protocol.auth.configs.jwt.JwtProvider;
import com.execute.protocol.auth.services.TokenService;
import com.execute.protocol.auth.services.UserDetailsServiceImpl;
import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.entities.User;
import com.execute.protocol.core.mappers.UserMapper;
import com.execute.protocol.core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    private final UserDetailsServiceImpl userDetails;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    public final UserRepository userRepository;
    private final TokenService tokenService;
    @Autowired
    public HomeController(UserDetailsServiceImpl userDetails, JwtProvider jwtProvider, UserDetailsService userDetailsService, UserRepository userRepository, TokenService tokenService) {
        this.userDetails = userDetails;
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }
    @GetMapping("/")
    public String indexPage(){
        return "index";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/user")
    @ResponseBody
    public Account userPage(){
        Account account = getCurrentAccount();
        return account;
    }
    @GetMapping("/enter")
    public String enter(HttpServletResponse response, HttpServletRequest request, Model model){

        String email = jwtProvider.getEmailFromToken(tokenService.getToken());
        Account account = userRepository.findByEmail(email);

        model.addAttribute("email", email);


        return "enter";
    }
    private Account getCurrentAccount() {
        String email;
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
