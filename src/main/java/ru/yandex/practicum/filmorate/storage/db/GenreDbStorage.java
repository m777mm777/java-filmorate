package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    final JdbcTemplate jdbcTemplate;

    public Genre getById(Long id) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre, id);

        if (genres.size() != 1) {
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

    public List<Film> addGenresToFilm(List<Film> films) {
        List<Film> filmWithGenres = new ArrayList<>();
        for (Film film: films) {

            Set<Genre> genres = getGenresByFilmId(film.getId());
            if (genres != null && genres.isEmpty()) genres.clear();

            film.setGenres(genres);
            filmWithGenres.add(film);
        }

        return filmWithGenres;
    }

    private Set<Genre> getGenresByFilmId(Long filmId) {
        String sqlQuery = "SELECT g.genre_id, g.genre_name FROM genres g" +
                "  INNER JOIN filmGenre f ON f.genre_id = g.genre_id" +
                "  WHERE film_id = ? " +
                "ORDER BY genre_id ASC;";

        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::rowMapGenre, filmId);

        return new HashSet<>(genres);
    }

    private Genre rowMapGenre(ResultSet resultSet, int i) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}