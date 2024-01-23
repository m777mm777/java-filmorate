package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void addLike(Long idFilm, Long idUser) {
            filmStorage.addLike(filmStorage.getById(idFilm).getId(), userStorage.getById(idUser).getId());
    }

    public void removeLike(Long idFilm, Long idUser) {
            filmStorage.removeLike(filmStorage.getById(idFilm).getId(), userStorage.getById(idUser).getId());
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

    public Film getById(Long id) {
        return filmStorage.getById(id);
    }

    public void deleteById(Long id) {
        filmStorage.deleteById(id);
    }

}