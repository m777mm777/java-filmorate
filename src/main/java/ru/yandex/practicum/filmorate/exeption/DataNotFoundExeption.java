package ru.yandex.practicum.filmorate.exeption;

public class DataNotFoundExeption extends RuntimeException {

    public DataNotFoundExeption(String message) {
        super(message);
    }
}
