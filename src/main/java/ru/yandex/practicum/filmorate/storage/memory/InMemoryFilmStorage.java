package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataIsNotValid;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.controller.FilmController.START_RELEASE_DATA;


@Component
public class InMemoryFilmStorage extends InMemoryBaseStorage<Film> implements FilmStorage {

    public void addLike(Long idFilm, Long idUser) {
        getById(idFilm).getLikes().add(idUser);
    }

    public void removeLike(Long idFilm, Long idUser) {
            getById(idFilm).getLikes().remove(idUser);
    }

    public List<Film> getFilmTopTenLike(Integer count) {

        return getAll().stream()
                .sorted((f1, f2) -> f2.quantityLike() - f1.quantityLike())
                .limit(count)
                .collect(Collectors.toList());
            }

    public void validate(Film data) {
        if (data.getReleaseDate().isBefore(START_RELEASE_DATA)) {
            throw new DataIsNotValid("Film release data is invalid");
        }
    }
}