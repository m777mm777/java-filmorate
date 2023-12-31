package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Validated
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    public static final LocalDate START_RELEASE_DATA = LocalDate.of(1895,12,25);

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Creating film {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Update film {}", film);
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Get film {}");
        return filmService.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Delete film {}");
        filmService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        log.info("Get film {}");
        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public List getFilmTopTenLike(@RequestParam(defaultValue = "10") @Positive Integer count) {
        log.info("Get get Film Top Like {}");
        return filmService.getFilmTopTenLike(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Put put Film Top Like {}");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Delit delit Film Top Like {}");
        filmService.removeLike(id, userId);
    }

}
