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
    public Tuple<Target, Event> initializer(JwtAuthentication principal) {

        Event event = eventRepository.findRandomEvent();

        User user = userRepository.findUserByStringId(principal.getStringId());
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
            @RequestParam(name = "answer", defaultValue = "0") long answerId,
            JwtAuthentication principal) {
        User user = userRepository.findUserByStringId(principal.getStringId());
        Event e = eventRepository.findById(eventId).get();
        //var e = events.stream().filter(w->w.getId() == eventId).findFirst().get();
        var a = e.getAnswers().stream().filter(w->w.getId() == answerId).findFirst().get();
        Target target = user.getTarget();
        a.getDoing().forEach(w->{

            switch (w.getActionTarget()){
                case MONEY ->  target.setMoney(target.getMoney() + w.getValueTarget());
                case POLLUTION -> target.setPollution(target.getPollution() + w.getValueTarget());

            }
        });

        userRepository.save(user);
        Event event = eventRepository.findRandomEvent();
       return new Tuple<>(user.getTarget(), eventRepository.findRandomEvent());
    }

}
