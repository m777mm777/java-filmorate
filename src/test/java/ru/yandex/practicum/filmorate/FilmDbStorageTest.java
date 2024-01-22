package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final MpaDbStorage mpaStorage;
    private final UserStorage userStorage;

    @Test
    void testSaveFilm() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(2L).build())
                .build();

        film = filmStorage.create(film);
        Mpa mpa = mpaStorage.getById(2L);
        assertThat(!mpa.equals(null));
        film.setMpa(mpa);

        Film film1 = filmStorage.getById(film.getId());

        assertThat(!film1.equals(null));
        assertThat(film.equals(film1));
    }

    @Test
    void testFindById() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(2L).build())
                .build();
        film.setId(1L);
        Film film1 = filmStorage.getById(1L);

        assertThat(film.getId().equals(film1.getId()));
        assertThat(film.getName().equals(film1.getName()));
        assertThat(film.getId().equals(film1.getId()));
    }

    @Test
    void testDeleteFilm() {
        Film film = Film.builder()
                .name("deleted film")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(1L).build())
                .build();

        film = filmStorage.create(film);

        Collection<Film> films = filmStorage.getAll();
        assertThat(films).hasSize(3);

        filmStorage.deleteById(film.getId());

        films = filmStorage.getAll();
        assertThat(films).hasSize(2);

    }

    @Test
    void testFindAll() {
        Collection<Film> films = filmStorage.getAll();
        assertThat(films).hasSize(1);
    }

    @Test
    void testUpdateFilm() {
        Film film = filmStorage.getById(1L);
        assertThat(!film.equals(null));

        Film film1 = film;
        film.setName("Update Name");

        filmStorage.update(film1);

        Film cheUpdate = filmStorage.getById(1L);

        assertThat(film.equals(cheUpdate));
    }

    @Test
    void testSaveLike() {
        User user = User.builder()
                .email("email@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);
        userStorage.create(user);

        filmStorage.addLike(1L, 1L);
        filmStorage.addLike(1L, 2L);

        Collection<Film> popularFilms = filmStorage.getFilmTopTenLike(10);
        Film film = filmStorage.getById(1L);
        assertThat(popularFilms).hasSize(1);
        assertThat(film.getLikes().size() == 2);
    }

    @Test
    void testDeleteLike() {
        Film film = filmStorage.getById(1L);
        assertThat(film.getLikes().size() == 2);
        filmStorage.removeLike(1L, 1L);

        assertThat(film.getLikes().size() == 1);
    }

    @Test
    void testFindPopularFilms() {
        Film film = Film.builder()
                .name("deleted film")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .mpa(Mpa.builder().id(1L).build())
                .build();

        filmStorage.create(film);

        filmStorage.addLike(1L, 1L);
        filmStorage.addLike(2L, 1L);
        filmStorage.addLike(2L, 2L);

        List<Film> popularFilms = filmStorage.getFilmTopTenLike(10);

        assertThat(popularFilms).hasSize(2);
        assertThat(popularFilms.get(0) == filmStorage.getById(2L));
    }
}
