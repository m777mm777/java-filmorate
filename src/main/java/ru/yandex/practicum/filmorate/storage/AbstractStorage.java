package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.List;

public interface AbstractStorage<T extends BaseUnit> {

    public abstract void validate(T data);
    T create(T data);

    T update(T data);

    List<T> getAll();

    T getById(Long id);

    void deleteById(Long id);

}
