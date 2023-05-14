package com.execute.protocol.core.exeptions;

import com.execute.protocol.core.helpers.JsonAnswer;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ControllerAdviceException extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        JsonAnswer jsonAnswer = new JsonAnswer("Не верный формат данных");

        return  ResponseEntity.ok(jsonAnswer);
    }
}
