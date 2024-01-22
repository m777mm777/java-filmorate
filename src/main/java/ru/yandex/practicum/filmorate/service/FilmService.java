package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void addLike(Optional<Long> idFilm, Optional<Long> idUser) {
            filmStorage.addLike(checkId(idFilm), userStorage.getById(checkId(idUser)).getId());
    }

    public void removeLike(Optional<Long> idFilm, Optional<Long> idUser) {
            filmStorage.removeLike(checkId(idFilm), userStorage.getById(checkId(idUser)).getId());
    }

    public List<Film> getFilmTopTenLike(Integer count) {
        return filmStorage.getFilmTopTenLike(count);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Optional<Long> id) {
        return filmStorage.getById(checkId(id));
    }

    public void deleteById(Optional<Long> id) {
        filmStorage.deleteById(checkId(id));
    }

    public Long checkId(Optional<Long> id) {
        return id.orElseThrow(() -> new DataNotFoundException("Не верный id Фильма"));
    }
}