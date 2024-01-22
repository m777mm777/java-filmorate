package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    public Genre getById(Long id);

    public List<Genre> getAll();

    public List<Genre> getGenresByFilmID(Long filmId);
}
