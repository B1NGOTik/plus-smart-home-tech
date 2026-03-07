package ru.yandex.practicum.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    /*@ExceptionHandler(RuntimeException.class)
    public String otherHandler(RuntimeException e) {
        return e.getMessage();
    }*/
}
