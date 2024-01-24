package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikeStorage {

    void addLike(Long idFilm, Long idUser);

    void removeLike(Long idFilm, Long idUser);

    List getFilmTopTenLike(Integer count);
}
