package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long idFilm , Long idUser) {
        filmStorage.addLike(idFilm,idUser);
    }

    public void removeLike(Long idFilm, Long idUser) {
        filmStorage.removeLike(idFilm, idUser);
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
