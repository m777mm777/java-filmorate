package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaStorage;

    @Test
    void testFindById() {
        Mpa mpa = Mpa.builder()
                .id(2L)
                .name("PG")
                .build();
        Mpa mpa11 = mpaStorage.getById(2L);

        assertThat(mpa).isEqualTo(mpa11);
    }

    @Test
    void testFindAll() {
        Collection<Mpa> genres = mpaStorage.getAll();
        assertThat(genres).hasSize(5);
    }

}