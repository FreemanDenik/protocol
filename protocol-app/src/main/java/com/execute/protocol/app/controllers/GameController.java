package com.execute.protocol.app.controllers;

import com.execute.protocol.app.models.GameInfo;
import com.execute.protocol.app.models.TwoTuple;
import com.execute.protocol.auth.models.JwtAuthentication;
import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.account.User;
import com.execute.protocol.core.exeptions.GameNullPointerException;
import com.execute.protocol.core.helpers.JsonAnswer;
import com.execute.protocol.core.mappers.EventMapper;
import com.execute.protocol.core.repositories.UserRepository;
import com.execute.protocol.core.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.execute.protocol.core.enums.EmErrors.*;

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
            Event randomEvent;
            // Получение пользователя по email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.warn(String.format("не найден email %s", email));
                        return new NullPointerException(String.format("не найден email %s", email));
                    });
            if (user.getCurrentEvent() > 0) {
                // Событие по id
                randomEvent = eventService.getByIdEvent(user.getCurrentEvent());
            } else {
                // Случайное событие Dto
                randomEvent = eventService.getRandomEvent(user.getId(), 0);
                // Устанавливаем id случайного события в user, тем самым делаем его текущим
                user.setCurrentEvent(randomEvent.getId());
            }

            // Время последней активности
            user.setLastAccountActivity(LocalDateTime.now());
            // Сохранить изменения
            userRepository.save(user);
            //userRepository.delete(user);
            Target target = user.getTarget();
            EventDto randomEventDto = EventMapper.INSTANCE.mapEventToDto(randomEvent);
            // Удаляем ответы которые записаны как одноразовые
            randomEventDto.getAnswers().removeIf(w-> user.getOnceAnswer().contains(w.getId()));

            setDisabledAnswers(randomEvent.getAnswers(), user.getThings(), randomEventDto);
            return ResponseEntity.ok(TwoTuple.of(target, randomEventDto));
        } catch (Exception e) {
            log.error("произошла ошибка: " + e.getMessage());
            jsonAnswer.addMessage(INITIALIZER,"произошла ошибка");
        }
        return ResponseEntity.ok(jsonAnswer);
    }

    /**
     * Метод контроллера выполняющий шаг игры
     *
     * @return
     */

    // BindingResult в обызательном порядке должен идти псразу после проверымой формы (класса)
    @PostMapping("go")
    public ResponseEntity<? super TwoTuple> go(
            // Модель получения значении eventId и answerId
            @Valid @RequestBody GameInfo gameInfo,
            BindingResult bindResult,
            // Текущий пользователь email login roles
            JwtAuthentication principal,
            JsonAnswer jsonAnswer)  {

        if (bindResult.hasErrors()) {
            log.warn("Ошибки при валидации:"+ bindResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
            jsonAnswer.addMessage(MODEL_BIND, bindResult.getAllErrors());
        } else {

            try {
                String email = principal.getEmail();
                // Получение пользователя по email
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> {
                            log.warn(String.format("не найден email %s", email));
                            return new GameNullPointerException(String.format("не найден email %s", email));
                        });
                // Пытаемся получить событие по полю "текущее событие" в модели User
                Event currentEvent = eventService.getById(user.getCurrentEvent())
                        .orElseThrow(() -> {
                            log.warn(String.format("не найдено событие по id %d", gameInfo.getEvent()));
                            return new GameNullPointerException("не найдено событие");
                        });
                // Пытаемся получить ответ по полученным данным id атвета
                Answer selectedAnswer = currentEvent.getAnswers().stream().filter(w -> w.getId() == gameInfo.getAnswer()).findFirst()
                        .orElseThrow(() -> {
                            log.warn(String.format("не найден ответ по id %d", gameInfo.getAnswer()));
                            return new GameNullPointerException("не найден ответ");
                        });
                // Проверяем принадлежит ли ответ к текущему событию
                eventService.isEventHasAnswer(user.getCurrentEvent(), gameInfo.getAnswer())
                        .orElseThrow(() -> {
                            log.warn(String.format("в событии '%s' нету ответа '%s'",
                                    currentEvent.getId() + " " + currentEvent.getEventText(),
                                    selectedAnswer.getId() + " " + selectedAnswer.getAnswerText()));
                            return new GameNullPointerException(String.format("в событии '%s' нету ответа '%s'",
                                    currentEvent.getId() + " " + currentEvent.getEventText(),
                                    selectedAnswer.getId() + " " + selectedAnswer.getAnswerText()));
                        });
                Event randomEvent;
                // если связанное событие идем по нему
                if (Objects.nonNull(selectedAnswer.getLinkEvent())){
                    user.setCurrentEvent(selectedAnswer.getLinkEvent().getId());
                    randomEvent = selectedAnswer.getLinkEvent();
                    log.info("Идем по связанному событию: " + randomEvent.getEventText());
                } else {
                    // Случайное событие Dto
                    randomEvent  = eventService.getRandomEvent(user.getId(), currentEvent.getId());
                    // Устанавливаем id случайного события в user, тем самым делаем его текущим
                    user.setCurrentEvent(randomEvent.getId());
                    log.info("Случайное событие: " + randomEvent.getEventText());
                }
                // Добавление скрытых событий если таковы есть в ответе
                if (!selectedAnswer.getOpenCarts().isEmpty()){
                    user.getAddEvents().addAll(selectedAnswer.getOpenCarts());
                    log.info("Добавление скрытых событий: " + selectedAnswer.getOpenCarts().stream().map(String::valueOf).collect(Collectors.joining(",")));
                }
                // Если событие одноразовое, то добавляем его в одноразовую таблицу
                if(currentEvent.isUseOnce()){
                    user.getOnceEvents().add(currentEvent.getId());
                }
                // Если ответ одноразовый, то добавляем его в одноразовую таблицу
                if (selectedAnswer.isUseOnce()){
                    user.getOnceAnswer().add(selectedAnswer.getId());
                }
                // Добавить категорию
                if(!selectedAnswer.getOpenCategories().isEmpty()){
                    user.getAddCategories().addAll(selectedAnswer.getOpenCategories());
                }
                // Удалить категорию
                if(!selectedAnswer.getCloseCategories().isEmpty()){
                    user.getAddCategories().removeAll(selectedAnswer.getCloseCategories());
                }
                // Забрать предмет
                if(!selectedAnswer.getTakeThings().isEmpty()){
                    user.getThings().removeAll(selectedAnswer.getTakeThings());
                }
                // Получить предмет
                if(!selectedAnswer.getGiveThings().isEmpty()){
                    user.getThings().addAll(selectedAnswer.getGiveThings());
                }
                Target target = user.getTarget();

                target.calcGld(selectedAnswer.getGold());
                target.calcRep(selectedAnswer.getReputation());
                target.calcInf(selectedAnswer.getInfluence());
                target.calcLck(selectedAnswer.getLuck());
                target.calcShd(selectedAnswer.getShadow());

                EventDto eventDto = EventMapper.INSTANCE.mapEventToDto(randomEvent);
                // Удаляем ответы которые записаны как одноразовые
                eventDto.getAnswers().removeIf(w-> user.getOnceAnswer().contains(w.getId()));

                // устанавливаем disabled ответам которые требуют предметы и их (одного из них) нет
                setDisabledAnswers(randomEvent.getAnswers(), user.getThings(), eventDto);

                userRepository.save(user);
                return ResponseEntity.ok(TwoTuple.of(user.getTarget(), eventDto));
            } catch (GameNullPointerException n) {
                log.error(n.getMessage());
                jsonAnswer.addMessage(GAME_NULL, n.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("произошла ошибка: " + e.getMessage());
                jsonAnswer.addMessage(GAME,"произошла ошибка");

            }
        }
        return ResponseEntity.ok(jsonAnswer);//new Tuple<>(user.getTarget(), randomEventDto);
    }

    /**
     * Устанавливаем disabled ответам которые требуют предметы и их (одного из них) нет
     *
     * @param answers
     * @param things
     * @param eventDto
     */
    private void setDisabledAnswers(Set<Answer> answers, Set<Integer> things, EventDto eventDto){
        answers.stream().filter(w->!w.getIfThings().isEmpty()).forEach(w->{
            if((!w.getIfThings().isEmpty() && things.isEmpty()) ||
                    !w.getIfThings().stream().allMatch(e->things.contains(e))){
                eventDto.getAnswers().stream().filter(e->e.getId() == w.getId()).findFirst().get().setEnabled(false);
            }
        });
    }
}
