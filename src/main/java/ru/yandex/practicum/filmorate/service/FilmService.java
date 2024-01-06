package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void addLike(Long idFilm, Long idUser) {

        if(checkIdFilm(idFilm) & checkIdUser(idUser)) {
            filmStorage.addLike(idFilm, idUser);
        }
    }

    public void removeLike(Long idFilm, Long idUser) {
        
        if(checkIdFilm(idFilm) & checkIdUser(idUser)) {

            filmStorage.removeLike(idFilm, idUser);
        }
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
        Long i = Optional.of(id).orElseThrow(() -> new DataNotFoundException("Не верный id фильма"));

        return filmStorage.getById(i);
    }

    public void deleteById(Long id) {
        Long i = Optional.of(id).orElseThrow(() -> new DataNotFoundException("Не верный id фильма"));

        filmStorage.deleteById(i);
    }

    private Boolean checkIdUser(Long id) {
        Long i = Optional.of(id).orElseThrow(() -> new DataNotFoundException("Не верный id пользователя"));

        if(userStorage.getById(i) == null) {
            throw new DataNotFoundException("Такого пользователя нет");
        }
        return true;
    }

    private Boolean checkIdFilm(Long id) {
        Long i = Optional.of(id).orElseThrow(() -> new DataNotFoundException("Не верный id фильма"));

        if(filmStorage.getById(i) == null) {
            throw new DataNotFoundException("Такого фильма нет");
        }
        return true;
    }

}
