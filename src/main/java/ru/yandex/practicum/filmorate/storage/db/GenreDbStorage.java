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
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

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

    static Genre createGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

    @Override
    public void load(List<Film> films) {
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));

        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final String sqlQuery = "SELECT * " +
                "FROM genres g, filmGenre fg WHERE fg.genre_id = g.genre_id AND fg.film_id IN (" + inSql + ")";

        jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            final Film film = filmById.get(rs.getLong("FILM_ID"));

            film.setGenres(film.getGenres());
            film.getGenres().add(makeGenre(rs, 0));

            return film;
        }, films.stream().map(Film::getId).toArray());

    }

    private Genre makeGenre(ResultSet resultSet, int i) throws SQLException {
        Genre genre = Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();

        return genre;
    }
}