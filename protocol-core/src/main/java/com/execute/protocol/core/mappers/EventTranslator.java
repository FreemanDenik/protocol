package com.execute.protocol.core.mappers;

import com.execute.protocol.core.dto.AnswerDto;
import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Image;
import com.execute.protocol.core.services.ThingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EventTranslator {
    private final ThingService thingService;
    public String imageToStringBase64(Image image) {
        return image != null ? Base64.getEncoder().encodeToString(image.getImage()) : null;
    }
//    private void setDisabledAnswers(Set<Answer> answers, Set<Integer> things, EventDto eventDto) {
//        answers.stream().filter(w -> !w.getIfThings().isEmpty()).forEach(w -> {
//            if ((!w.getIfThings().isEmpty() && things.isEmpty()) ||
//                    !w.getIfThings().stream().allMatch(e -> things.contains(e))) {
//                AnswerDto answerDto = eventDto.getAnswers().stream().filter(e -> e.getId() == w.getId()).findFirst().get();
//                answerDto.setEnabled(false);
//                // Получаем названия предметов, что есть и нет в наличии и заполняем ими answerDto
//                Map<Boolean, Set<String>> result = w.getIfThings().stream().collect(Collectors.partitioningBy(c->things.contains(c), Collectors.toSet()))
//                        .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, c->thingService.getThings(c.getValue()).stream().map(t->t.getTitle()).collect(Collectors.toSet())));
//                answerDto.setHaveThings(result.get(true));
//                answerDto.setNeedThings(result.get(false));
//            }
//        });
//    }
}
