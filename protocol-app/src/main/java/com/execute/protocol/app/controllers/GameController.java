package com.execute.protocol.app.controllers;

import com.execute.protocol.app.models.Tuple;
import com.execute.protocol.auth.dto.JwtAuthentication;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.acc.User;
import com.execute.protocol.core.repositories.EventRepository;
import com.execute.protocol.core.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/game")
public class GameController {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public GameController(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Метод контроллера первый шаг игры
     * @return
     */
    @PostMapping("initializer")
    public Tuple<Target, Event> initializer(Principal principal) {

        JwtAuthentication tt = (JwtAuthentication)principal;
        Event event = eventRepository.findRandomEvent();
        String email = ""; // get mail
        User user = userRepository.findByEmail( email);
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
    @PostMapping("go")
    public Tuple<Target, Event> go(
             @RequestParam(name = "event", defaultValue = "0") long eventId,
             @RequestParam(name = "answer", defaultValue = "0") long answerId) {
        String email = ""; // get mail
        User account = userRepository.findByEmail(email);
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
