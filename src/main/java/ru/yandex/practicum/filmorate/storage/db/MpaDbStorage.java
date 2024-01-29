package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    final JdbcTemplate jdbcTemplate;

    public Mpa getById(Long id) {
        String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
        List<Mpa> mpas = jdbcTemplate.query(sqlQuery, MpaDbStorage::createMpa, id);

        if (mpas.size() != 1) {
            throw new DataNotFoundException("В базе mpa по данному id нет");
        }

        return mpas.get(0);
    }

    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM mpa ORDER BY mpa_id ASC";
        return jdbcTemplate.query(sqlQuery, MpaDbStorage::createMpa);
    }

    static Mpa createMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }
}