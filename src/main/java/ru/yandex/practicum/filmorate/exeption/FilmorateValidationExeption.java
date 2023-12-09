package ru.yandex.practicum.filmorate.exeption;

public class FilmorateValidationExeption extends RuntimeException {

    public FilmorateValidationExeption(String message) {
        super(message);
    }
}
