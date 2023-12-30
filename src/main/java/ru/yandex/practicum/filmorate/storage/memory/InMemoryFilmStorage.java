package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataIsNotValid;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.controller.FilmController.START_RELEASE_DATA;


@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage extends InMemoryBaseStorage<Film> implements FilmStorage {

    private UserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addLike(Long idFilm, Long idUser) {
        Film film = getById(idFilm);
        User user = userStorage.getById(idUser);
            if (film != null && user != null) {
                getById(idFilm).getLikes().add(idUser);
            } else {
                throw new DataNotFoundException("Фильм или пользователь не найден");
            }
    }

    public void removeLike(Long idFilm, Long idUser) {
        if (getById(idFilm) != null && userStorage.getById(idUser) != null) {
            getById(idFilm).getLikes().remove(idUser);
        } else {
            throw new DataNotFoundException("Фильм или пользователь не найден");
        }
    }

    public List<Film> getFilmTopTenLike(Integer count) {

        return getAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
            }

    public void validate(Film data) {
        if (data.getReleaseDate().isBefore(START_RELEASE_DATA)) {
            throw new DataIsNotValid("Film release data is invalid");
        }
    }
}
