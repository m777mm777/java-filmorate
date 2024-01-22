package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public Genre getById(Long id) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre, id);

        if (genres.size() > 1 || genres.size() < 1) {
            throw new DataNotFoundException("В базе genre по данному id нет");
        }

        return genres.get(0);
    }

    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genres ORDER BY genre_id ASC";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre);
    }

    public List<Genre> getGenresByFilmID(Long filmId) {
        String sqlQuery
                = "SELECT g.genre_id, g.genre_name " +
                "FROM genres AS g " +
                "INNER JOIN filmGenre AS fg ON fg.genre_id = g.genre_id WHERE film_id = ? ORDER BY genre_id ASC";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre, filmId);

        return genres;
    }

    static Genre createGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

}