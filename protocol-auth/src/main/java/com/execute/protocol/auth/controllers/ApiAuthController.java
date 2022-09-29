package com.execute.protocol.auth.controllers;


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


    // ЭТОТ КОНТРОЛЛЕР ПОКА НИГДЕ НЕ ИСПОЛЬЗУЕТСЯ!!!
    @GetMapping("/loginByJwt")
    public void loginByJwt(HttpServletResponse response) throws IOException {

        response.sendRedirect("/game");


    }
}
