package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    void testFindById() {
        MpaStorage mpaStorage = new MpaDbStorage(jdbcTemplate);
        Mpa mpa = Mpa.builder()
                .id(2L)
                .name("PG")
                .build();
        Mpa mpa11 = mpaStorage.getById(2L);

        assertThat(mpa).isEqualTo(mpa11);
    }

    @Test
    void testFindAll() {
        MpaStorage mpaStorage = new MpaDbStorage(jdbcTemplate);
        Collection<Mpa> genres = mpaStorage.getAll();
        assertThat(genres).hasSize(5);
    }
}