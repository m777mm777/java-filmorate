package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    public Genre getById(Long id);

    public List<Genre> getAll();

    public List<Genre> getGenresByFilmID(Long filmId);

    public List<Film> addGenresToFilm(List<Film> films);
}
