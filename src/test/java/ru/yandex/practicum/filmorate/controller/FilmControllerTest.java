package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.exeption.DataIsNotValid;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FilmControllerTest {

    private FilmService filmService;

    @Autowired
    public FilmControllerTest(FilmService filmService) {
        this.filmService = filmService;
    }

    @Test
    void getAllFilm() {
        Film film = Film.builder()
                .name("терминатор2")
                .description("восстали машины")
                .releaseDate(LocalDate.of(1999,12,01))
                .duration(100)
                .build();

        filmService.create(film);

        Film film2 = Film.builder()
                .id(1L)
                .name("терминатор2")
                .description("восстали машины")
                .releaseDate(LocalDate.of(1999,12,01))
                .duration(100)
                .build();

        List<Film> collectionFilm = new ArrayList<>();
        collectionFilm.add(film2);

        Assertions.assertEquals(filmService.getAll(),collectionFilm);
    }

    @Test
    void creeteFilm() {
        Film film = Film.builder()
                .name("терминатор2")
                .description("восстали машины")
                .releaseDate(LocalDate.of(1999,12,01))
                .duration(100)
                .build();

        filmService.create(film);

        Film film2 = Film.builder()
                .id(1L)
                .name("терминатор2")
                .description("восстали машины")
                .releaseDate(LocalDate.of(1999,12,01))
                .duration(100)
                .build();

        List<Film> collectionFilm = new ArrayList<>();
        collectionFilm.add(film2);

        Assertions.assertEquals(filmService.getAll(),collectionFilm);
    }

    @Test
    void updateFilm() {
        Film film = Film.builder()
                .name("терминатор2")
                .description("восстали машины")
                .releaseDate(LocalDate.of(1999,12,01))
                .duration(100)
                .build();

        filmService.create(film);

        Film film2 = Film.builder()
                .id(1L)
                .name("терминатор2")
                .description("восстали машины2")
                .releaseDate(LocalDate.of(1999,12,01))
                .duration(100)
                .build();

        filmService.update(film2);

        List<Film> collectionFilm = new ArrayList<>();
        collectionFilm.add(film2);

        Assertions.assertEquals(filmService.getAll(),collectionFilm);
    }

    @Test
    void validate() {
        Film film = Film.builder()
                .name("терминатор2")
                .description("восстали машины")
                .releaseDate(LocalDate.of(1999,12,01))
                .duration(100)
                .build();
        filmService.create(film);
    }

    @Test
    void validateNegative() {
        Film film = Film.builder()
                .name("терминатор2")
                .description("восстали машины")
                .releaseDate(LocalDate.of(1800,12,01))
                .duration(100)
                .build();
        Assertions.assertThrows(DataIsNotValid.class, () -> filmService.create(film));
    }
}
