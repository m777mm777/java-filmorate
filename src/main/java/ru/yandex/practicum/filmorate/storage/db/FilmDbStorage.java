package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmDbStorage extends BaseFilmAndLikeDb implements FilmStorage {

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<Film> create(Film film) {
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
            batchInsert(film);
        }

        List<Film> films = new ArrayList<>();
        films.add(film);
        return films;
    }

    public List<Film> update(Film film) {
        Film film1 = getById(film.getId()).get(0);
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
            batchInsert(film);
        }

        List<Film> films = new ArrayList<>();
        films.add(film);
        return films;
    }

    private int[] batchInsert(Film film) {
        List<Genre> genres = new ArrayList<>();
        for (Genre i : film.getGenres()) {
            genres.add(i);
        }

        return this.jdbcTemplate.batchUpdate(
                "INSERT INTO filmGenre (film_id, genre_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {

                        Long id = genres.get(i).getId();
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
    public List<Film> getById(Long id) {
        String sqlQuery = "SELECT f.film_id, f.film_name, f.film_description, f.release_date, " +
                "f.duration, f.mpa_id, m.mpa_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id = ?";

        List<Film> films = jdbcTemplate.query(sqlQuery, this::rowMapFilm, id);

        if (films.size() != 1) {
            throw new DataNotFoundException("Ошибка по данному id нет фильма или в нем ошибка");
        }

        return films;
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(query, id);
    }
}