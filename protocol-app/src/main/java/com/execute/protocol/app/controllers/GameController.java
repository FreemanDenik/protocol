package com.execute.protocol.app.controllers;

import com.execute.protocol.app.models.GameInfo;
import com.execute.protocol.app.models.TwoTuple;
import com.execute.protocol.auth.models.JwtAuthentication;
import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.account.User;
import com.execute.protocol.core.helpers.JsonAnswer;
import com.execute.protocol.core.mappers.EventMapper;
import com.execute.protocol.core.repositories.UserRepository;
import com.execute.protocol.core.services.EventService;
import com.execute.protocol.core.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/game")
@RequiredArgsConstructor
@Log4j2
public class GameController {
    private final EventService eventService;
    private final UserRepository userRepository;

    /**
     * Метод контроллера первый шаг игры (инициализация)
     *
     * @return
     */
    @PostMapping("initializer")
    public ResponseEntity initializer(
            // Текущий пользователь email login roles
            JwtAuthentication principal,
            JsonAnswer jsonAnswer) {
        try {
            String email = principal.getEmail();
            EventDto randomEventDto;
            // Получение пользователя по email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.warn(String.format("не найден email %s", email));
                        return new NullPointerException(String.format("не найден email %s", email));
                    });
            if (user.getCurrentEvent() > 0) {
                // Событие по id
                randomEventDto = eventService.getByIdEventDto(user.getCurrentEvent());
            } else {
                // Случайное событие Dto
                randomEventDto = eventService.getRandomEventDto(user.getAddCategories());
                // Устанавливаем id случайного события в user, тем самым делаем его текущим
                user.setCurrentEvent(randomEventDto.getId());
            }

            // Время последней активности
            user.setLastAccountActivity(LocalDateTime.now());
            // Сохранить изменения
            userRepository.save(user);
            //userRepository.delete(user);
            Target target = user.getTarget();

            return ResponseEntity.ok(TwoTuple.of(target, randomEventDto));
        } catch (Exception e) {
            log.error("произошла ошибка: " + e.getMessage());
            jsonAnswer.addMessage("произошла ошибка");
        }
        return ResponseEntity.ok(jsonAnswer);
    }

    /**
     * Метод контроллера выполняющий шаг игры
     *
     * @return
     */
    @PostMapping("go")
    public ResponseEntity go(
            // Модель получения значении eventId и answerId
            @Valid @RequestBody GameInfo gameInfo,
            // Текущий пользователь email login roles
            JwtAuthentication principal,
            BindingResult bindResult,
            JsonAnswer jsonAnswer) {
        if (bindResult.hasErrors()) {
            log.warn("Ошибки при валиации:"+ bindResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
            jsonAnswer.addMessage(bindResult.getAllErrors());
        } else {

            try {
                String email = principal.getEmail();
                // Получение пользователя по email
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> {
                            log.warn(String.format("не найден email %s", email));
                            return new NullPointerException(String.format("не найден email %s", email));
                        });
                // Пытаемся получить событие по полю "текущее событие" в модели User
                Event currentEvent = eventService.getById(user.getCurrentEvent())
                        .orElseThrow(() -> {
                            log.warn(String.format("не найдено событие по id %d", gameInfo.getEvent()));
                            return new NullPointerException("не найдено событие");
                        });
                // Пытаемся получить ответ по полученным данным id атвета
                Answer selectedAnswer = currentEvent.getAnswers().stream().filter(w -> w.getId() == gameInfo.getAnswer()).findFirst()
                        .orElseThrow(() -> {
                            log.warn(String.format("не найден ответ по id %d", gameInfo.getAnswer()));
                            return new NullPointerException("не найден ответ");
                        });
                // Проверяем принадлежит ли ответ к текущему событию
                eventService.isEventHasAnswer(user.getCurrentEvent(), gameInfo.getAnswer())
                        .orElseThrow(() -> {
                            log.warn(String.format("в событии '%s' нету ответа '%s'",
                                    currentEvent.getId() + " " + currentEvent.getEventText(),
                                    selectedAnswer.getId() + " " + selectedAnswer.getAnswerText()));
                            return new NullPointerException(String.format("в событии '%s' нету ответа '%s'",
                                    currentEvent.getId() + " " + currentEvent.getEventText(),
                                    selectedAnswer.getId() + " " + selectedAnswer.getAnswerText()));
                        });
                EventDto randomEventDto;
                if (Objects.nonNull(selectedAnswer.getLinkEvent())){
                    user.setCurrentEvent(selectedAnswer.getLinkEvent().getId());
                    randomEventDto = EventMapper.INSTANCE.mapEventToDto(selectedAnswer.getLinkEvent());
                } else {
                    // Случайное событие Dto
                    randomEventDto  = eventService.getRandomEventDto(user.getAddCategories());
                    // Устанавливаем id случайного события в user, тем самым делаем его текущим
                    user.setCurrentEvent(randomEventDto.getId());
                }
                userRepository.save(user);
                return ResponseEntity.ok(TwoTuple.of(user.getTarget(), randomEventDto));
            } catch (NullPointerException n) {
                jsonAnswer.addMessage(n.getMessage());
            } catch (Exception e) {
                log.error("произошла ошибка: " + e.getMessage());
                jsonAnswer.addMessage("произошла ошибка");

            }
        }
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
        //userRepository.save(user);
        return ResponseEntity.ok(jsonAnswer);//new Tuple<>(user.getTarget(), randomEventDto);
    }

}
