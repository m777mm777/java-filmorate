package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataIsNotValid;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
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
    private final MpaDbStorage mpaDbStorage;
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
            String query = "INSERT INTO filmGenre (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(query, film.getId(), genre.getId());
            }
        }

        film.setGenres(genreStorage.getGenresByFilmID(film.getId()));

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
            String sqlDelete2 = "DELETE FROM filmGenre WHERE film_id = ?";
            jdbcTemplate.update(sqlDelete2, film.getId());
            String sqlUpdate = "INSERT INTO filmGenre (film_id, genre_id) VALUES (?, ?)";
            List<Long> check = new ArrayList<>();

            for (Genre genre : film.getGenres()) {
                if (!check.contains(genre.getId())) {
                    check.add(genre.getId());
                    jdbcTemplate.update(sqlUpdate, film.getId(), genre.getId());
                }
            }

        }

        film.setGenres(genreStorage.getGenresByFilmID(film.getId()));

        return film;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::createFilm);
    }

    @Override
    public Film getById(Long id) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::createFilm, id);
        if (films.size() > 1 || films.size() < 1) {
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

    private Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaDbStorage.getById(rs.getLong("mpa_id")))
                .likes(getLikerByFilmId(rs.getLong("film_id")))
                .genres(genreStorage.getGenresByFilmID(rs.getLong("film_id")))
                .build();
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

}