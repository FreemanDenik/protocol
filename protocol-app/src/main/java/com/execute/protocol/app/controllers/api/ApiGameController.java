package com.execute.protocol.app.controllers.api;

import com.execute.protocol.app.models.Tuple;
import com.execute.protocol.app.testStart.StartEvent;
import com.execute.protocol.auth.configs.jwt.JwtProvider;
import com.execute.protocol.auth.services.TokenService;
import com.execute.protocol.auth.services.UserDetailsServiceImpl;
import com.execute.protocol.core.entities.Account;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.User;
import com.execute.protocol.core.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Random;

@RestController
public class ApiGameController {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    public ApiGameController(UserRepository userRepository, UserDetailsServiceImpl userDetailsService, TokenService tokenService, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/api/initializer")
    public Tuple<Target, Event> initializer() {
        List<Event> events = StartEvent.getEvents();
        Random random = new Random();
        Event event1 = events.get(random.nextInt(events.size()));

        User user = userRepository.findByEmail( jwtProvider.getEmailFromToken(tokenService.getToken()));
        user.setActualEvent(event1.getId());
        userRepository.save(user);
       return new Tuple<>(user.getTarget(), event1);
    }
    @PostMapping("/api/game")
    public Tuple<Target, Event> game(

             @RequestParam(name = "event", defaultValue = "0") long eventId,
             @RequestParam(name = "answer", defaultValue = "0") long answerId) {
        User account = userRepository.findByEmail(jwtProvider.getEmailFromToken(tokenService.getToken()));
        List<Event> events = StartEvent.getEvents();
        var e = events.stream().filter(w->w.getId() == eventId).findFirst().get();
        var a = e.getAnswers().stream().filter(w->w.getId() == answerId).findFirst().get();
        Target target = account.getTarget();
        a.getDoing().forEach(w->{

            switch (w.getActionTarget()){
                case MONEY ->  target.setMoney(target.getMoney() + w.getValueTarget());
                case POLLUTION -> target.setPollution(target.getPollution() + w.getValueTarget());

            }
        });

        userRepository.save(account);
        Random random = new Random();
        Event event = events.get(random.nextInt(events.size()));
       return new Tuple<>(account.getTarget(), event);
    }

    @GetMapping("/api/increase")
    public int increase(Principal principal) {

        User user =  userRepository.findByEmail(getCurrentAccountMail());
       Target target = Target.builder().money(100).build();
       user.setTarget(target);
       userRepository.save(user);

        User user1 =  userRepository.findByEmail(getCurrentAccountMail());
        Target target1 = user1.getTarget();
        userRepository.delete(user1);
       return 1;
    }

    private String getCurrentAccountMail() {
        String email;
        var tt = SecurityContextHolder.getContext().getAuthentication().toString();
        if (SecurityContextHolder.getContext().getAuthentication().toString().contains("given_name")) {
            email = ((DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = (Account) auth.getPrincipal();
            email = account.getEmail();
        }

        return email;
    }
}
