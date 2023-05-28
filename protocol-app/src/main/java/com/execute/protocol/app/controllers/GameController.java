package com.execute.protocol.app.controllers;

import com.execute.protocol.app.models.GameConsumer;
import com.execute.protocol.app.models.GameProducer;
import com.execute.protocol.auth.models.JwtAuthentication;
import com.execute.protocol.core.dto.AnswerDto;
import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.dto.ThingDto;
import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.account.User;
import com.execute.protocol.core.exeptions.GameNullPointerException;
import com.execute.protocol.core.exeptions.GameOverException;
import com.execute.protocol.core.helpers.JsonAnswer;
import com.execute.protocol.core.mappers.EventMapper;
import com.execute.protocol.core.mappers.ThingMapper;
import com.execute.protocol.core.models.RestartGame;
import com.execute.protocol.core.repositories.UserRepository;
import com.execute.protocol.core.services.EventService;
import com.execute.protocol.core.services.ThingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.execute.protocol.core.enums.EmErrors.*;

@RestController
@RequestMapping(value = "api/game")
@RequiredArgsConstructor
@Log4j2
public class GameController {
    private final EventService eventService;
    private final UserRepository userRepository;
    private final ThingService thingService;
    private final EventMapper eventMapper;
    private final Random random = new Random();

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
                randomEvent = eventService.getByIdEvent(user.getCurrentEvent())
                        .orElseThrow(() -> new GameNullPointerException(String.format("Не найдено текущее событие пользователя по id %s", user.getCurrentEvent())));
            } else {
                // Случайное событие
                // метод getByIdEvent имеет настройку кеширования, однако она не работает если вызвать ее через другой метод,
                // потому такая конструкция сначала берем id а потом уже получаем объект, либо из бд, либо из кеш (getRandomEventId->getByIdEvent)
                Integer randomEventId = eventService.getRandomEventId(user.getId(), 0)
                        .orElseThrow(() -> new GameNullPointerException("Ошибка при получении id случайного события"));
                randomEvent = eventService.getByIdEvent(randomEventId)
                        .orElseThrow(() -> new GameNullPointerException("Ошибка при получении события по id " + randomEventId));
//                randomEvent = eventService.getRandomEvent(user.getId(), 0)
//                        .orElseThrow(()-> new GameNullPointerException("Ошибка при получении случайного события"));
                // Устанавливаем id случайного события в user, тем самым делаем его текущим
                user.setCurrentEvent(randomEvent.getId());
            }
            // Время последней активности
            user.setLastAccountActivity(LocalDateTime.now());
            // Сохранить изменения
            userRepository.save(user);
            EventDto randomEventDto = eventMapper.mapEventToDto(randomEvent);
            // Удаляем ответы которые записаны как одноразовые
            randomEventDto.getAnswers().removeIf(w -> user.getOnceAnswer().contains(w.getId()));

            setDisabledAnswers(randomEvent.getAnswers(), user.getThings(), randomEventDto);
            // Конверт предметов в Dto
            Set<ThingDto> thingDto = ThingMapper.INSTANCE.mapThingToDto(thingService.getThings(user.getThings()));
            return ResponseEntity.ok(new GameProducer(user.getTarget(), randomEventDto, thingDto, user.getDeadShans()));
            //return ResponseEntity.ok(TwoTuple.of(target, randomEventDto));
        } catch (Exception e) {
            log.error("произошла ошибка: " + e.getMessage());
            jsonAnswer.addErrors(INITIALIZER, "произошла ошибка");
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
    public ResponseEntity go(
            // Модель получения значении eventId и answerId
            @Valid @RequestBody GameConsumer gameConsumer,
            BindingResult bindResult,
            // Текущий пользователь email login roles
            JwtAuthentication principal,
            JsonAnswer jsonAnswer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        if (bindResult.hasErrors()) {
            log.warn("Ошибки при валидации:" + bindResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
            jsonAnswer.addErrors(MODEL_BIND, bindResult.getAllErrors());
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
//                Event currentEvent = eventService.getByIdEvent(user.getCurrentEvent())
//                        .orElseThrow(() -> {
//                            log.warn(String.format("не найдено событие по id %d", user.getCurrentEvent()));
//                            return new GameNullPointerException("не найдено событие");
//                        });
                // Если текущее события не нашли то обнуляем его у пользователя и делаем инициализацию
                Optional<Event> checkedEvent = eventService.getByIdEvent(user.getCurrentEvent());
                if (!checkedEvent.isPresent()) {
                    executor.execute(() -> {
                        user.setCurrentEvent(0);
                        userRepository.save(user);
                    });
                    return initializer(principal, jsonAnswer);
                }
                Event currentEvent = checkedEvent.get();

                // Пытаемся получить ответ по полученным данным id ответа
                Answer selectedAnswer = currentEvent.getAnswers().stream().filter(w -> w.getId() == gameConsumer.getAnswer()).findFirst()
                        .orElseThrow(() -> {
                            log.warn(String.format("не найден ответ по id %d относящийся к событию %d", gameConsumer.getAnswer(), currentEvent.getId()));
                            return new GameNullPointerException("не найден ответ");
                        });
                // Проверка доступности целевого ответа из отсутствия или нехватки требуемых предметов
                if (!isDisabledAnswers(selectedAnswer, user.getThings())) {
                    log.warn(String.format("Ответ %d заблокирован ввиду отсутствия необходимых предметов %s", selectedAnswer.getId(), user.getThings().stream().map(String::valueOf).collect(Collectors.joining(","))));
                    throw new GameNullPointerException("Ответ заблокирован ввиду отсутствия необходимых предметов");
                }
                // Проверяем принадлежит ли ответ к текущему событию
//                eventService.isEventHasAnswer(user.getCurrentEvent(), gameConsumer.getAnswer())
//                        .orElseThrow(() -> {
//                            log.warn(String.format("в событии '%s' нету ответа '%s'",
//                                    currentEvent.getId() + " " + currentEvent.getEventText(),
//                                    selectedAnswer.getId() + " " + selectedAnswer.getAnswerText()));
//                            return new GameNullPointerException(String.format("в событии '%s' нету ответа '%s'",
//                                    currentEvent.getId() + " " + currentEvent.getEventText(),
//                                    selectedAnswer.getId() + " " + selectedAnswer.getAnswerText()));
//                        });
                // Проверка конца игры
                String endGame = endGame(user.getTarget(), user.getDeadShans());
                if (Objects.nonNull(endGame)) {
                    executor.execute(new RestartGame(user));
                    jsonAnswer.endGame(endGame);
                    userRepository.save(user);
                } else {
                    Event randomEvent;
                    // если связанное событие идем по нему
                    if (Objects.nonNull(selectedAnswer.getLinkEvent())) {
                        user.setCurrentEvent(selectedAnswer.getLinkEvent().getId());
                        randomEvent = selectedAnswer.getLinkEvent();
                        log.info("Идем по связанному событию: " + randomEvent.getEventText());
                    } else {

                        // Случайное событие
                        // метод getByIdEvent имеет настройку кеширования, однако она не работает если вызвать ее через другой метод,
                        // потому такая конструкция сначала берем id а потом уже получаем объект, либо из бд, либо из кеш (getRandomEventId->getByIdEvent)
                        Integer randomEventId = eventService.getRandomEventId(user.getId(), currentEvent.getId())
                                .orElseThrow(() -> new GameNullPointerException("Ошибка при получении id случайного события"));
                        randomEvent = eventService.getByIdEvent(randomEventId)
                                .orElseThrow(() -> new GameNullPointerException("Ошибка при получении события по id " + randomEventId));

                        // Устанавливаем id случайного события в user, тем самым делаем его текущим
                        user.setCurrentEvent(randomEvent.getId());
                        log.info("Случайное событие: " + randomEvent.getEventText());
                    }
                    // Добавление скрытых событий если таковы есть в ответе
                    if (!selectedAnswer.getOpenCarts().isEmpty()) {
                        user.getAddEvents().addAll(selectedAnswer.getOpenCarts());
                        log.info("Добавление скрытых событий: " + selectedAnswer.getOpenCarts().stream().map(String::valueOf).collect(Collectors.joining(",")));
                    }
                    // Если событие одноразовое, то добавляем его в одноразовую таблицу
                    if (currentEvent.isUseOnce()) {
                        user.getOnceEvents().add(currentEvent.getId());
                        log.info("Одноразовое событие id: " + currentEvent.getId());
                    }
                    // Если ответ одноразовый, то добавляем его в одноразовую таблицу
                    if (selectedAnswer.isUseOnce()) {
                        user.getOnceAnswer().add(selectedAnswer.getId());
                        log.info("Одноразовый ответ id: " + selectedAnswer.getId());
                    }
                    // Добавить категории
                    if (!selectedAnswer.getOpenCategories().isEmpty()) {
                        user.getAddCategories().addAll(selectedAnswer.getOpenCategories());
                        log.info("Добавление категории Set id: " + selectedAnswer.getOpenCategories().stream().map(String::valueOf).collect(Collectors.joining(",")));
                    }
                    // Удалить категории
                    if (!selectedAnswer.getCloseCategories().isEmpty()) {
                        user.getAddCategories().removeAll(selectedAnswer.getCloseCategories());
                        log.info("Удаление категории Set id: " + selectedAnswer.getCloseCategories().stream().map(String::valueOf).collect(Collectors.joining(",")));
                    }
                    // Забрать предметы
                    if (!selectedAnswer.getTakeThings().isEmpty()) {
                        user.getThings().removeAll(selectedAnswer.getTakeThings());
                        log.info("Забираем предметы ids: " + selectedAnswer.getTakeThings().stream().map(String::valueOf).collect(Collectors.joining(",")));
                    }
                    // Получить предметы
                    if (!selectedAnswer.getGiveThings().isEmpty()) {
                        user.getThings().addAll(selectedAnswer.getGiveThings());
                        log.info("Получить предметы ids: " + selectedAnswer.getGiveThings().stream().map(String::valueOf).collect(Collectors.joining(",")));
                    }
                    Target target = user.getTarget();

                    target.calcGld(selectedAnswer.getGold());
                    target.calcRep(selectedAnswer.getReputation());
                    target.calcInf(selectedAnswer.getInfluence());
                    target.calcLck(selectedAnswer.getLuck());
                    target.calcShd(selectedAnswer.getShadow());
                    log.info("Установка целей добавить/вычесть в соответствии с событием");

                    EventDto eventDto = eventMapper.mapEventToDto(randomEvent);
                    // Удаляем ответы которые записаны как одноразовые
                    eventDto.getAnswers().removeIf(w -> user.getOnceAnswer().contains(w.getId()));

                    // устанавливаем disabled ответам которые требуют предметы и их (одного из них) нет
                    setDisabledAnswers(randomEvent.getAnswers(), user.getThings(), eventDto);
                    log.info("Устанавливаем disabled ответам которые требуют предметы и их (одного из них) нет");

                    user.setLastAccountActivity(LocalDateTime.now());

                    userRepository.save(user);

                    // Конверт предметов в Dto
                    Set<ThingDto> thingDto = ThingMapper.INSTANCE.mapThingToDto(thingService.getThings(user.getThings()));

                    return ResponseEntity.ok(new GameProducer(user.getTarget(), eventDto, thingDto, user.getDeadShans()));
                }
            } catch (GameOverException n) {
                log.info(n.getMessage());

                jsonAnswer.endGame(n.getMessage());
            } catch (GameNullPointerException n) {
                log.error(n.getMessage());
                jsonAnswer.addErrors(GAME_NULL, n.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("произошла ошибка: " + e.getMessage());
                jsonAnswer.addErrors(GAME, "произошла ошибка");

            }
        }

        return ResponseEntity.ok(jsonAnswer);//new Tuple<>(user.getTarget(), randomEventDto);
    }

    /**
     * Окончена игра или нет
     *
     * @param target
     * @return
     */
    private String endGame(Target target, byte deadShans) throws GameOverException {
        // Проверяем порог смерти(окончания игры)
        boolean isDie = random.nextInt(1, 100) < deadShans;
        String result = null;
        if (target.getGold() <= 0 && isDie) {
            // золото меньше 0
            result = "золото меньше 0";
        } else if (target.getGold() >= 100 && isDie) {
            // золото больше 100
            result = "золото больше 100";
        } else if (target.getReputation() <= 0 && isDie) {

            result = "репутация меньше 0";
        } else if (target.getReputation() >= 100 && isDie) {

            result = "репутация больше 100";
        } else if (target.getInfluence() <= 0 && isDie) {

            result = "влияние меньше 0";
        } else if (target.getInfluence() >= 100 && isDie) {

            result = "влияние больше 100";
        } else if (target.getShadow() <= 0 && isDie) {

            result = "скрытность меньше 0";
        } else if (target.getShadow() >= 100 && isDie) {

            result = "скрытность больше 100";
        } else if (target.getLuck() <= 0 && isDie) {

            result = "удача меньше 0";
        } else if (target.getLuck() >= 100 && isDie) {

            result = "удача больше 100";
        }
        return result;
    }

    /**
     * Устанавливаем disabled ответам которые требуют предметы и их (одного из них) нет
     *
     * @param answers
     * @param things
     * @param eventDto
     */
    private void setDisabledAnswers(Set<Answer> answers, Set<Integer> things, EventDto eventDto) {
        answers.stream().filter(w -> !w.getIfThings().isEmpty()).forEach(w -> {
//            if ((!w.getIfThings().isEmpty() && things.isEmpty()) ||
//                    !w.getIfThings().stream().allMatch(e -> things.contains(e))) {
            if (!isDisabledAnswers(w, things)) {
                AnswerDto answerDto = eventDto.getAnswers().stream().filter(e -> e.getId() == w.getId()).findFirst().get();
                answerDto.setEnabled(false);
                // Получаем названия предметов, что есть и нет в наличии и заполняем ими answerDto
                Map<Boolean, Set<String>> result = w.getIfThings().stream().collect(Collectors.partitioningBy(c -> things.contains(c), Collectors.toSet()))
                        .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, c -> thingService.getThings(c.getValue()).stream().map(t -> t.getTitle()).collect(Collectors.toSet())));
                answerDto.setHaveThings(result.get(true));
                answerDto.setNeedThings(result.get(false));
            }
        });
    }

    private boolean isDisabledAnswers(Answer answers, Set<Integer> things) {
        return ((!answers.getIfThings().isEmpty() && things.isEmpty()) ||
                !answers.getIfThings().stream().allMatch(e -> things.contains(e))) ? false : true;
    }
}
