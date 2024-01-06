package ru.yandex.practicum.filmorate.storage.memory;

import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.exeption.OtherExceptions;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.*;

public class InMemoryBaseStorage<T extends BaseUnit> implements AbstractStorage<T> {

    private final Map<Long,T> storage = new HashMap<>();
    private long generateId;

    @Override
    public T create(T data) {
        validate(data);
        data.setId(++generateId);
        storage.put(data.getId(),data);
        return data;
    }

    @Override
    public T update(T data) {
        if (!storage.containsKey(data.getId())) {
            throw new OtherExceptions("Data not found");
        }

        validate(data);
        storage.put(data.getId(),data);
        return data;
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public T getById(Long id) {

        if (!storage.containsKey(id)) {
            throw new DataNotFoundException("Data not found");
        }
        return storage.get(id);
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public void validate(T data) {
    }

}