package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    public void addLike(Long idFilm, Long idUser) {
        likeStorage.addLike(filmStorage.getById(idFilm).getId(), userStorage.getById(idUser).getId());
    }

    public void removeLike(Long idFilm, Long idUser) {
        likeStorage.removeLike(filmStorage.getById(idFilm).getId(), userStorage.getById(idUser).getId());
    }

    public List<Film> getFilmTopTenLike(Integer count) {
        return likeStorage.getFilmTopTenLike(count);
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