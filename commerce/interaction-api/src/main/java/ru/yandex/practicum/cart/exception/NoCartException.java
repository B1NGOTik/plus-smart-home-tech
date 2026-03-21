package ru.yandex.practicum.cart.exception;

public class NoCartException extends RuntimeException {
    public NoCartException(String message) {
        super(message);
    }
}
