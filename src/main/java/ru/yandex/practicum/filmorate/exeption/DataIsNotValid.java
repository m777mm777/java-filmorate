package ru.yandex.practicum.filmorate.exeption;

public class DataIsNotValid extends RuntimeException {
    public DataIsNotValid(String message) {
        super(message);
    }
}
