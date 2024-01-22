package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreStorage;

    @Test
    void testFindById() {
        Genre genre = Genre.builder()
                .id(4L)
                .name("Триллер")
                .build();
        Genre genre1 = genreStorage.getById(4L);

        assertThat(genre).isEqualTo(genre1);
    }

    @Test
    void testFindAll() {
        Collection<Genre> genres = genreStorage.getAll();
        assertThat(genres).hasSize(6);
    }

}