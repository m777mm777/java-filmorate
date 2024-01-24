package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseFilmAndLikeDb {

    final JdbcTemplate jdbcTemplate;

    public BaseFilmAndLikeDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected Film rowMapFilm(ResultSet rs, int i) throws SQLException {
        Mpa mpa = rowMapMpa(rs, i);

        Set<Genre> genres = getGenresByFilmId(rs.getLong("film_id"));
        if (genres != null && genres.isEmpty()) genres.clear();

        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .genres(genres)
                .build();
    }

    private Mpa rowMapMpa(ResultSet rs, int i) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }

    protected Set<Genre> getGenresByFilmId(Long filmId) {
        String sqlQuery = "SELECT g.genre_id, g.genre_name FROM genres g" +
                "  INNER JOIN filmGenre f ON f.genre_id = g.genre_id" +
                "  WHERE film_id IN (?) " +
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