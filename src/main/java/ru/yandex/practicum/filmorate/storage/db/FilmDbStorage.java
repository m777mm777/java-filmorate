package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataIsNotValid;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.controller.FilmController.START_RELEASE_DATA;

@Component
public class FilmDbStorage extends BaseFilmAndLikeDb implements FilmStorage {

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Film create(Film film) {
        validate(film);

        String sql = "INSERT INTO films (film_name, film_description, release_date, duration, mpa_id)" +
                " VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
                    ps.setString(1, film.getName());
                    ps.setString(2, film.getDescription());
                    ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                    ps.setInt(4, film.getDuration());
                    ps.setLong(5, film.getMpa().getId());
                    return ps;
                }, keyHolder);
        film.setId((long) keyHolder.getKey().intValue());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            batchInsert(checkingForGenreRepetition(film.getGenres()), film);
        }

        film.setGenres(getGenresByFilmId(film.getId()));

        return film;
    }

    @Override
    public Film update(Film film) {
        Film film1 = getById(film.getId());
        String sqlQuery = "update films set " +
                "film_name = ?, film_description = ?, release_date = ?, duration = ?, mpa_id = ?  where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            String sqlDelete = "DELETE FROM filmGenre WHERE film_id = ?";
            jdbcTemplate.update(sqlDelete, film.getId());
        } else {
            String sqlDelete = "DELETE FROM filmGenre WHERE film_id = ?";
            jdbcTemplate.update(sqlDelete, film.getId());
            batchInsert(checkingForGenreRepetition(film.getGenres()), film);
        }

        film.setGenres(getGenresByFilmId(film.getId()));

        return film;
    }

    private int[] batchInsert(List<Long> genres, Film film) {

        return this.jdbcTemplate.batchUpdate(
                "INSERT INTO filmGenre (film_id, genre_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {

                        Long id = genres.get(i);
                        ps.setLong(1, film.getId());
                        ps.setLong(2, id);
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.film_id, f.film_name, f.film_description, f.release_date, " +
                "f.duration, f.mpa_id, m.mpa_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id";

        return jdbcTemplate.query(sqlQuery, this::rowMapFilm);
    }

    @Override
    public Film getById(Long id) {

        String sqlQuery = "SELECT f.film_id, f.film_name, f.film_description, f.release_date, " +
                "f.duration, f.mpa_id, m.mpa_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id = ?";

        List<Film> films = jdbcTemplate.query(sqlQuery, this::rowMapFilm, id);

        if (films.size() != 1) {
            throw new DataNotFoundException("Ошибка по данному id нет фильма или в нем ошибка");
        }

        return films.get(0);
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(query, id);
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATA)) {
            throw new DataIsNotValid("Film release data is invalid");
        }
    }

    private List<Long> checkingForGenreRepetition(Set<Genre> genres) {
        List<Long> check = new ArrayList<>();
        for (Genre genre : genres) {
            if (!check.contains(genre.getId())) {
                check.add(genre.getId());
            }
        }
        return check;
    }
}