package com.execute.protocol.app.controllers;

import com.execute.protocol.app.models.GameInfo;
import com.execute.protocol.app.models.Tuple;
import com.execute.protocol.auth.models.JwtAuthentication;
import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.account.User;
import com.execute.protocol.core.repositories.UserRepository;
import com.execute.protocol.core.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "api/game", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GameController {
    private final UserRepository userRepository;
    private final EventService eventService;
    /**
     * Метод контроллера первый шаг игры (инициализация)
     * @return
     */
    @PostMapping("initializer")
    public Tuple<Target, EventDto> initializer(
            // Текущий пользователь email login roles
            JwtAuthentication principal) {

        String email = principal.getEmail();
        EventDto randomEventDto;
        // Получаем user по email
        User user = userRepository.findByEmail(email);
        if (user.getCurrentEvent() > 0){
            // Событие по id
            randomEventDto = eventService.getByIdEventDto(user.getCurrentEvent());

        }else {
            // Случайное событие Dto
            randomEventDto = eventService.getRandomEventDto();
            // Устанавливаем id случайного события в user, тем самым делаем его текущим
            user.setCurrentEvent(randomEventDto.getId());
        }

        // Время последней активности
        user.setLastAccountActivity(LocalDateTime.now());
        // Сохранить изменения
        userRepository.save(user);
        //userRepository.delete(user);
        Target target = user.getTarget();
        //target.setUser(null);
        return new Tuple<>(target, randomEventDto);
    }

    /**
     * Метод контроллера выполняющий шаг игры
     * @return
     */
    @PostMapping("go")

    public Tuple<Target, EventDto> go(
            // Модель получения значении eventId и answerId
            @RequestBody GameInfo gameInfo,
            // Текущий пользователь email login roles
            JwtAuthentication principal) {
        String email = principal.getEmail();
        // Получение пользователя по email и event, проверяя event страхуемся от подстановки иных значении
        User user = userRepository.findByEmailAndEvent(email, gameInfo.getEvent());
        // Поиск события по id которое держим в себе запись user
//        Event event = eventService.getById(user.getCurrentEvent());
//        // Из события получаем варианты ответов и из них получаем один вариант ответа
//        Answer answer = event.getAnswers().stream().filter(w -> w.getId() == gameInfo.getAnswer()).findFirst().get();
//        // Создаем ссылку на Target (что бы внести изменения, они отразятся в базе)
//        Target target = user.getTarget();
//        // Перебираем действия ответа и выполняем их
//        answer.getDoing().forEach(w -> {
//
//            switch (w.getActionTarget()) {
//                case GOLD -> target.setGold(target.getGold() + w.getValueTarget());
//                case REPUTATION -> target.setReputation(target.getReputation() + w.getValueTarget());
//                case THIRST -> target.setThirst(target.getThirst() + w.getValueTarget());
//                case FIGHT -> target.setFight(target.getFight() + w.getValueTarget());
//                case SHADOW -> target.setShadow(target.getShadow() + w.getValueTarget());
//            }
//
//        });
//        // Случайное событие Dto
//        EventDto randomEventDto = eventService.getRandomEventDto();
//        // Устанавливаем id нового случайного события в user
//        user.setCurrentEvent(randomEventDto.getId());
//        // Время последней активности
//        user.setLastAccountActivity(LocalDateTime.now());
        // Сохранить изменения
        userRepository.save(user);
        return null;//new Tuple<>(user.getTarget(), randomEventDto);
    }

}
