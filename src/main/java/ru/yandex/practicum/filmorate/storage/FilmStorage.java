package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends AbstractStorage<Film> {

    void addLike(Long idFilm, Long idUser);

    void removeLike(Long idFilm, Long idUser);

    List getFilmTopTenLike(Integer count);
}