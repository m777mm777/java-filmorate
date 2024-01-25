package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends AbstractStorage<Film> {
    List<Film> create(Film data);

    List<Film> update(Film data);

    List<Film> getById(Long id);
}
