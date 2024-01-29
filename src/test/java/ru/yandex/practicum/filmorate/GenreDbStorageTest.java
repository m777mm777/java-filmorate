package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    void testFindById() {
        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        Genre genre = Genre.builder()
                .id(4L)
                .name("Триллер")
                .build();
        Genre genre1 = genreStorage.getById(4L);

        assertThat(genre).isEqualTo(genre1);
    }

    @Test
    void testFindAll() {
        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        Collection<Genre> genres = genreStorage.getAll();
        assertThat(genres).hasSize(6);
    }
}