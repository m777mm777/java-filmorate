package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataIsNotValid;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.controller.FilmController.START_RELEASE_DATA;

@Primary
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

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

    @Override
    public void addLike(Long idFilm, Long idUser) {
        String query = "DELETE FROM likes WHERE film_id = ? AND user_id = ? ";
        jdbcTemplate.update(query, idFilm, idUser);
        query = "INSERT INTO likes (film_id, user_id) VALUES(?, ?) ";
        jdbcTemplate.update(query, idFilm, idUser);
    }

    @Override
    public void removeLike(Long idFilm, Long idUser) {
        String query = "DELETE FROM likes WHERE film_id = ? AND user_id = ? ";
        jdbcTemplate.update(query, idFilm, idUser);
    }

    @Override
    public List<Film> getFilmTopTenLike(Integer count) {
        return getAll().stream()
                .sorted((f1, f2) -> f2.quantityLike() - f1.quantityLike())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Set<Long> getLikerByFilmId(Long filmId) {
        String query = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Long> likes = jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("user_id"), filmId);
        return new HashSet<>(likes);
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATA)) {
            throw new DataIsNotValid("Film release data is invalid");
        }
    }

    private List<Long> checkingForGenreRepetition(List<Genre> genres) {
        List<Long> check = new ArrayList<>();
        for (Genre genre : genres) {
            if (!check.contains(genre.getId())) {
                check.add(genre.getId());
            }
        }
        return check;
    }

    private Film rowMapFilm(ResultSet rs, int i) throws SQLException {
        Mpa mpa = rowMapMpa(rs, i);

        List<Genre> genres = getGenresByFilmId(rs.getLong("film_id"));
        if (genres != null && genres.isEmpty()) genres.clear();

        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .likes(getLikerByFilmId(rs.getLong("film_id")))
                .genres(genres)
                .build();
    }

    private Mpa rowMapMpa(ResultSet rs, int i) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }

    private List<Genre> getGenresByFilmId(Long filmId) {
        String sqlQuery = "SELECT g.genre_id, g.genre_name FROM genres g" +
                "  INNER JOIN filmGenre f ON f.genre_id = g.genre_id" +
                "  WHERE film_id IN (?) " +
                "ORDER BY genre_id ASC;";

        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::rowMapGenre, filmId);

        if (genres.isEmpty()) {
            genres = new ArrayList<>();
        }
        return genres;
    }

    private Genre rowMapGenre(ResultSet resultSet, int i) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}