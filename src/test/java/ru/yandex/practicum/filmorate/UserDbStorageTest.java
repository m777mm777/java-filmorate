package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    void testSaveUser() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = User.builder()
                .email("email@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        user = userStorage.create(user);
        User user1 = userStorage.getById(user.getId());

        assertThat(!user.equals(null));
        assertThat(user).isEqualTo(user1);
    }

    @Test
    void testGetById() {
        User user = User.builder()
                .email("ema34il@ya.ru")
                .login("logggin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        UserStorage userStorage = new UserDbStorage(jdbcTemplate);

        user = userStorage.create(user);
        User user1 = userStorage.getById(user.getId());

        assertThat(user.getId().equals(user1.getId()));
        assertThat(user.getEmail().equals(user1.getEmail()));
        assertThat(user.getLogin().equals(user1.getLogin()));
    }

    @Test
    void testDeleteUser() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = User.builder()
                .email("delete_user")
                .login("login-delete-user")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        user = userStorage.create(user);

        Collection<User> users = userStorage.getAll();
        assertThat(users).hasSize(1);

        userStorage.deleteById(user.getId());

        users = userStorage.getAll();
        assertThat(users).hasSize(0);
    }

    @Test
    void testFindAll() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = User.builder()
                .email("deledte_user")
                .login("logdin-delete-user")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);
        User user1 = User.builder()
                .email("delete_user")
                .login("login-delete-user")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user1);

        Collection<User> users = userStorage.getAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void testUpdateUser() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = User.builder()
                .email("deledte_user")
                .login("logdin-delete-user")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);

        User user2 = userStorage.getById(4L);;
        user2.setName("Update Name");

        userStorage.update(user2);

        User chek = userStorage.getById(4L);

        assertThat(!chek.equals(null));
        assertThat(user2.getName().equals("Update Name"));
    }

    @Test
    void testSaveFriend() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        FriendsStorage friendsStorage = new FriendsDbStorage(jdbcTemplate);

        User user = User.builder()
                .email("emetetttail@ya.ru")
                .login("lotetettgin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);

        User user2 = User.builder()
                .email("emeretetttail@ya.ru")
                .login("lerotetettgin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user2);

        friendsStorage.addFriend(8L, 9L);

        List<User> friends = friendsStorage.getUserFriends(8L);
        assertThat(friends).hasSize(1);
    }

    @Test
    void testDeleteFriend() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        FriendsStorage friendsStorage = new FriendsDbStorage(jdbcTemplate);
        User user = User.builder()
                .email("emyyahhil@ya.ru")
                .login("logyyhhin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);

        User user2 = User.builder()
                .email("emyyeeeahhil@ya.ru")
                .login("logwwyyhhin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user2);

        friendsStorage.addFriend(6L, 7L);
        Collection<User> friends = friendsStorage.getUserFriends(6L);

        assertThat(friends).hasSize(1);

        friendsStorage.removeFriend(6L, 7L);

        friends = friendsStorage.getUserFriends(1L);

        assertThat(friends).hasSize(0);
    }

    @Test
    void testGetFriends() {
        FriendsStorage friendsStorage = new FriendsDbStorage(jdbcTemplate);
        Collection<User> friends = friendsStorage.getUserFriends(1L);
        assertThat(friends).hasSize(0);
    }

    @Test
    void testFindCommonFriends() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        FriendsStorage friendsStorage = new FriendsDbStorage(jdbcTemplate);

        User user = User.builder()
                .email("emyy1ahhil@ya.ru")
                .login("logy1yhhin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);
        User user2 = User.builder()
                .email("emy2yahhil@ya.ru")
                .login("log2yyhhin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user2);
        User user3 = User.builder()
                .email("emy3yahhil@ya.ru")
                .login("log3yyhhin")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user3);

        friendsStorage.addFriend(11L, 12L);
        friendsStorage.addFriend(13L, 12L);

        List<User> commonFriends = friendsStorage.getMutualFriends(11L, 13L);

        assertThat(commonFriends).hasSize(1);
        assertThat(commonFriends.get(0).getId() == 12L);
    }
}