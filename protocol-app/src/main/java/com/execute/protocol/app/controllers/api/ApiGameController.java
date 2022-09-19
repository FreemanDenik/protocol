package com.execute.protocol.app.controllers.api;

import com.execute.protocol.app.models.Tuple;
import com.execute.protocol.auth.configs.jwt.JwtProvider;
import com.execute.protocol.auth.services.TokenService;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.acc.User;
import com.execute.protocol.core.repositories.EventRepository;
import com.execute.protocol.core.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class ApiGameController {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    public ApiGameController(UserRepository userRepository, EventRepository eventRepository, TokenService tokenService, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.tokenService = tokenService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Метод контроллера первый шаг игры
     * @return
     */
    @PostMapping("/initializer")
    public Tuple<Target, Event> initializer() {

        Event event = eventRepository.findRandomEvent();

        User user = userRepository.findByEmail( jwtProvider.getEmailFromToken(tokenService.getToken()));
        user.setCurrentEvent(event.getId());
        userRepository.save(user);
       return new Tuple<>(user.getTarget(), event);
    }

    /**
     * Метод контроллера выполняющий шаг игры
     * @param eventId
     * @param answerId
     * @return
     */
    @PostMapping("/go")
    public Tuple<Target, Event> go(
             @RequestParam(name = "event") long eventId,
             @RequestParam(name = "answer") long answerId) {
        User account = userRepository.findByEmail(jwtProvider.getEmailFromToken(tokenService.getToken()));
        Event e = eventRepository.findById(eventId).get();
        //var e = events.stream().filter(w->w.getId() == eventId).findFirst().get();
        var a = e.getAnswers().stream().filter(w->w.getId() == answerId).findFirst().get();
        Target target = account.getTarget();
        a.getDoing().forEach(w->{

            switch (w.getActionTarget()){
                case MONEY ->  target.setMoney(target.getMoney() + w.getValueTarget());
                case POLLUTION -> target.setPollution(target.getPollution() + w.getValueTarget());

            }
        });

        userRepository.save(account);
        Event event = eventRepository.findRandomEvent();
       return new Tuple<>(account.getTarget(), eventRepository.findRandomEvent());
    }

}
