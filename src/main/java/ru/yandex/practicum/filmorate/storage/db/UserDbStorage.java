package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDbStorage extends BaseUserAndFriendDb implements UserStorage {

    final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {

        String sqlQuery = "insert into users (user_name, user_login, email, birthday) " +
                    "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
                        stmt.setString(1, user.getName());
                        stmt.setString(2, user.getLogin());
                        stmt.setString(3, user.getEmail());
                        stmt.setDate(4, Date.valueOf(user.getBirthday()));
                        return stmt;
            }, keyHolder);

        return getById(keyHolder.getKey().longValue());
    }

    @Override
    public User update(User user) {
        User use1 = getById(user.getId());
        String sqlQuery = "update users set user_name = ?, user_login = ?, email = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(),
                user.getId());
        return getById(user.getId());
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::createUser);
    }

    @Override
    public User getById(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, this::createUser, id);
        if (users.size() != 1) {
            throw new DataNotFoundException("Ошибка по данному id нет пользователя или в нем ошибка");
        }

        return users.get(0);
    }

    @Override
    public void deleteById(Long id) {
        User user = getById(id);
        String sqlQuery = "delete from users where user_id = ?;";
        jdbcTemplate.update(sqlQuery, user.getId());
    }
}