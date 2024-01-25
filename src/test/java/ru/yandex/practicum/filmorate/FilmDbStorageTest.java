package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    void testSaveFilm() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        MpaStorage mpaStorage = new MpaDbStorage(jdbcTemplate);

        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(2L).build())
                .genres(new HashSet<>())
                .build();

        filmStorage.create(film);
        Mpa mpa = mpaStorage.getById(2L);
        assertThat(!mpa.equals(null));
        film.setMpa(mpa);

        Film film1 = filmStorage.getById(film.getId()).get(0);

        assertThat(!film1.equals(null));
        assertThat(film.equals(film1));
    }

    @Test
    void testFindById() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);

        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(2L).build())
                .genres(new HashSet<>())
                .build();
        film.setId(1L);

        filmStorage.create(film);

        Film film1 = filmStorage.getById(8L).get(0);

        assertThat(film.getId().equals(film1.getId()));
        assertThat(film.getName().equals(film1.getName()));
        assertThat(film.getId().equals(film1.getId()));
    }

    @Test
    void testDeleteFilm() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);

        Film film = Film.builder()
                .name("deleted film")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(1L).build())
                .genres(new HashSet<>())
                .build();

        film = filmStorage.create(film).get(0);

        Collection<Film> films = filmStorage.getAll();
        assertThat(films).hasSize(1);

        filmStorage.deleteById(film.getId());

        films = filmStorage.getAll();
        assertThat(films).hasSize(0);

    }

    @Test
    void testFindAll() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);

        Film film = Film.builder()
                .name("deleted film")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(1L).build())
                .genres(new HashSet<>())
                .build();

        filmStorage.create(film);

        Collection<Film> films = filmStorage.getAll();
        assertThat(films).hasSize(1);
    }

    @Test
    void testUpdateFilm() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);

        Film film = Film.builder()
                .name("deleted film")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(1L).build())
                .genres(new HashSet<>())
                .build();

        filmStorage.create(film);

        Film film2 = filmStorage.getById(5L).get(0);
        assertThat(!film.equals(null));

        Film film1 = film2;
        film.setName("Update Name");

        filmStorage.update(film1);

        Film cheUpdate = filmStorage.getById(5L).get(0);

        assertThat(film.equals(cheUpdate));
    }

    @Test
    void testSaveLike() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        LikeStorage likeStorage = new LikeDbStorage(jdbcTemplate);

        User user = User.builder()
                .email("email@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        User user2 = User.builder()
                .email("emeeail@ya.ru")
                .login("loggin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);
        userStorage.create(user2);

        Film film = Film.builder()
                .name("deleted film")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(1L).build())
                .genres(new HashSet<>())
                .build();

        filmStorage.create(film);
        filmStorage.create(film);

        likeStorage.addLike(filmStorage.getAll().get(0).getId(), userStorage.getAll().get(0).getId());
        likeStorage.addLike(filmStorage.getAll().get(1).getId(), userStorage.getAll().get(0).getId());
        likeStorage.addLike(filmStorage.getAll().get(1).getId(), userStorage.getAll().get(1).getId());

        Collection<Film> popularFilms = likeStorage.getFilmTopTenLike(10);
        Film film1 = ((List<Film>) popularFilms).get(0);

        assertThat(film1.getId() == 3);
    }

    @Test
    void testDeleteLike() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        LikeStorage likeStorage = new LikeDbStorage(jdbcTemplate);

        User user = User.builder()
                .email("email@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        User user2 = User.builder()
                .email("emeeail@ya.ru")
                .login("loggin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);
        userStorage.create(user2);

        Film film = Film.builder()
                .name("deleted film")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(1L).build())
                .genres(new HashSet<>())
                .build();

        filmStorage.create(film);
        filmStorage.create(film);

        likeStorage.addLike(filmStorage.getAll().get(0).getId(), userStorage.getAll().get(0).getId());
        likeStorage.addLike(filmStorage.getAll().get(1).getId(), userStorage.getAll().get(0).getId());
        likeStorage.addLike(filmStorage.getAll().get(1).getId(), userStorage.getAll().get(1).getId());

        Collection<Film> popularFilms1 = likeStorage.getFilmTopTenLike(10);
        Film film2 = ((List<Film>) popularFilms1).get(0);

        assertThat(film2.getId() == 11);

        likeStorage.removeLike(filmStorage.getAll().get(1).getId(), userStorage.getAll().get(0).getId());
        likeStorage.removeLike(filmStorage.getAll().get(1).getId(), userStorage.getAll().get(0).getId());

        Collection<Film> popularFilms = likeStorage.getFilmTopTenLike(10);
        Film film1 = ((List<Film>) popularFilms).get(0);

        assertThat(film1.getId() == 10);
    }

    @Test
    void testFindPopularFilms() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        LikeStorage likeStorage = new LikeDbStorage(jdbcTemplate);

        Film film = Film.builder()
                .name("deleted film")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(1L).build())
                .genres(new HashSet<>())
                .build();

        filmStorage.create(film);
        filmStorage.create(film);

        User user = User.builder()
                .email("emeeail@ya.ru")
                .login("loggin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);
        User user2 = User.builder()
                .email("emereeail@ya.ru")
                .login("loerggin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user2);

        likeStorage.addLike(filmStorage.getAll().get(0).getId(), userStorage.getAll().get(0).getId());
        likeStorage.addLike(filmStorage.getAll().get(1).getId(), userStorage.getAll().get(0).getId());
        likeStorage.addLike(filmStorage.getAll().get(1).getId(), userStorage.getAll().get(1).getId());

        List<Film> popularFilms = likeStorage.getFilmTopTenLike(10);

        assertThat(popularFilms).hasSize(2);
        assertThat(popularFilms.get(0) == filmStorage.getById(filmStorage.getAll().get(0).getId()));
    }
}
