package ru.yandex.practicum.filmorate.exeption;

public class NoEntityException extends RuntimeException {
    public NoEntityException(String message) {
        super(message);
    }
}
