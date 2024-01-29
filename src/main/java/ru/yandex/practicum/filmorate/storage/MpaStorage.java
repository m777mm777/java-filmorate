package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    public Mpa getById(Long id);

    public List<Mpa> getAll();
}
