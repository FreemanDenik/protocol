package com.execute.protocol.core.helpers;

import com.execute.protocol.core.enums.EmErrors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Component
@RequestScope
public class JsonAnswer {
    public JsonAnswer(String error) {
        this.errors.add(error);
    }
    private int code;
    private List<String> errors = new ArrayList<>();
    public void addMessage(EmErrors code, List<org.springframework.validation.ObjectError> errors){
        this.errors.addAll(errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()));
        this.code = code.getCode();
    }
    public void addMessage(EmErrors code, String error){
        errors.add(error);
        this.code = code.getCode();
    }
    public void addMessage(Collection<String> errors){
        this.errors.addAll(errors);
    }

}
