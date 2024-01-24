package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendsDbStorage extends BaseUserAndFriendDb implements FriendsStorage {

    final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long id, Long friendId) {
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM friends WHERE friend_id = ?";
        jdbcTemplate.update(sqlQuery, friendId);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        String sqlQuery = "SELECT * FROM users AS u where u.user_id IN " +
                "(SELECT f.friend_id FROM friends AS f WHERE f.user_id = ?);";
        return jdbcTemplate.query(sqlQuery, this::createUser, id);
    }

    @Override
    public List<User> getMutualFriends(Long id, Long otherId) {
        String sqlQuery = "SELECT * FROM users \n" +
                "WHERE user_id IN (SELECT f.friend_id\n" +
                "FROM friends AS f \n" +
                "INNER JOIN friends AS f2 \n" +
                "ON f.friend_id = f2.friend_id\n" +
                "AND f.user_id = ?\n" +
                "AND f2.user_id = ?);";

        return jdbcTemplate.query(sqlQuery, this::createUser, id, otherId);
    }

}
