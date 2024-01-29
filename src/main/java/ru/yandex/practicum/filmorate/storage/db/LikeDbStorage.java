package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Component
public class LikeDbStorage extends BaseFilmAndLikeDb implements LikeStorage {

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void addLike(Long idFilm, Long idUser) {
        String query = "INSERT INTO likes (film_id, user_id) VALUES(?, ?) ";
        jdbcTemplate.update(query, idFilm, idUser);
    }

    @Override
    public void removeLike(Long idFilm, Long idUser) {
        String query = "DELETE FROM likes WHERE film_id = ? AND user_id = ? ";
        jdbcTemplate.update(query, idFilm, idUser);
    }

    @Override
    public List<Film> getFilmTopTenLike(Integer count) {

        String sqlQuery = "SELECT f.film_id, f.film_name, f.film_description, f.release_date, f.duration, " +
                "f.mpa_id, m.mpa_name, COUNT(l.film_id) AS count_like " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                " WHERE 1=1 " +
                " GROUP BY f.film_id " +
                "ORDER BY count_like DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, this::rowMapFilm, count);
    }
}