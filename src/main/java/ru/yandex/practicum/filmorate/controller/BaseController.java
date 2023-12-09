package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exeption.DataNotFoundExeption;
import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("checkstyle:Regexp")
public abstract class BaseController<T extends BaseUnit> {

    private final Map<Long,T> storage = new HashMap<>();
    private long generateId;

    public abstract void validate(T data);

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public T update(T data) {
        if (!storage.containsKey(data.getId())) {
            throw new DataNotFoundExeption("Data not found");

        }

        validate(data);
        storage.put(data.getId(),data);
        return data;
    }

    public T create(T data) {
        validate(data);
        data.setId(++generateId);
        storage.put(data.getId(),data);
        return data;
    }
}
