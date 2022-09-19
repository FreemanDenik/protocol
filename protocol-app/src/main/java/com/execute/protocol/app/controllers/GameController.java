package com.execute.protocol.app.controllers;

import com.execute.protocol.auth.configs.jwt.JwtProvider;
import com.execute.protocol.auth.services.TokenService;
import com.execute.protocol.core.entities.acc.Account;
import com.execute.protocol.core.entities.acc.Role;
import com.execute.protocol.core.repositories.AccountRepository;
import com.execute.protocol.core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class GameController {

    private final JwtProvider jwtProvider;
    public final UserRepository userRepository;
    public final AccountRepository accountRepository;
    private final TokenService tokenService;
    @Autowired
    public GameController(JwtProvider jwtProvider, UserRepository userRepository, AccountRepository accountRepository, TokenService tokenService) {

        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
    }
    @GetMapping("/")
    public String index(){
        return "game/index";
    }

    /**
     * Метод контроллера входная точка игры
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/game")
    public String enter(HttpServletResponse response, Model model) throws IOException {

        String email  = jwtProvider.getEmailFromToken(tokenService.getToken());
        Account account = accountRepository.findByEmail(email);
        // Если пользователь не имеет роли USER выпроваживаем его в login
        if (account.getRole() != Role.ROLE_USER)
            response.sendRedirect("/login");

        model.addAttribute("firstName", account.getFirstName());

        return "game/game";
    }


//    private Account getCurrentAccount() {
//        String email;
//        var tt = SecurityContextHolder.getContext().getAuthentication().toString();
//        if (SecurityContextHolder.getContext().getAuthentication().toString().contains("given_name")) {
//            email = ((DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
//        } else {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            Account account = (Account) auth.getPrincipal();
//            email = account.getEmail();
//        }
//
//        return (Account) userDetails.loadUserByUsername(email);
//    }
}
