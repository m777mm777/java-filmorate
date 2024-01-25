package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.DataIsNotValid;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.controller.FilmController.START_RELEASE_DATA;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;

    public void addLike(Long idFilm, Long idUser) {
        likeStorage.addLike(filmStorage.getById(idFilm).get(0).getId(), userStorage.getById(idUser).getId());
    }

    public void removeLike(Long idFilm, Long idUser) {
        likeStorage.removeLike(filmStorage.getById(idFilm).get(0).getId(), userStorage.getById(idUser).getId());
    }

    public List<Film> getFilmTopTenLike(Integer count) {
        return genreStorage.addGenresToFilm(likeStorage.getFilmTopTenLike(count));
    }

    public Film create(Film film) {
        validate(film);
        return genreStorage.addGenresToFilm(filmStorage.create(film)).get(0);
    }

    public Film update(Film film) {
        validate(film);
        return genreStorage.addGenresToFilm(filmStorage.update(film)).get(0);
    }

    public List<Film> getAll() {
        return genreStorage.addGenresToFilm(filmStorage.getAll());
    }

    public Film getById(Long id) {
        return genreStorage.addGenresToFilm(filmStorage.getById(id)).get(0);
    }

    public void deleteById(Long id) {
        filmStorage.deleteById(id);
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATA)) {
            throw new DataIsNotValid("Film release data is invalid");
        }
    }
}