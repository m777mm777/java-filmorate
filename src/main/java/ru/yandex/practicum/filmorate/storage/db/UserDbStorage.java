package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Primary
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void validate(User user) {
        if (isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public User create(User user) {
        validate(user);

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
        if (users.size() > 1 || users.size() < 1) {
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

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = getById(id);
        User friend = getById(friendId);
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?,?)";

            jdbcTemplate.update(sqlQuery, user.getId(), friend.getId());
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        User user = getById(id);
        User friend = getById(friendId);
        String sqlQuery = "DELETE FROM friends WHERE friend_id = ?";

        jdbcTemplate.update(sqlQuery, friend.getId());
    }

    @Override
    public List<User> getUserFriends(Long id) {
        User user = getById(id);
        String sqlQuery = "SELECT * FROM users AS u where u.user_id IN " +
                "(SELECT f.friend_id FROM friends AS f WHERE f.user_id = ?);";
        return jdbcTemplate.query(sqlQuery, this::createUser, id);
    }

    @Override
    public List<User> getMutualFriends(Long id, Long otherId) {
        User user = getById(id);
        User otherUser = getById(otherId);
        String sqlQuery = "SELECT * FROM users \n" +
                "WHERE user_id IN (SELECT f.friend_id\n" +
                "FROM friends AS f \n" +
                "INNER JOIN friends AS f2 \n" +
                "ON f.friend_id = f2.friend_id\n" +
                "AND f.user_id = ?\n" +
                "AND f2.user_id = ?);";

        return jdbcTemplate.query(sqlQuery, this::createUser, user.getId(), otherUser.getId());
    }

    private User createUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .name(rs.getString("user_name"))
                .login(rs.getString("user_login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

}