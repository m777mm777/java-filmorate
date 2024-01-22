package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    void testSaveUser() {
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
                .email("email@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        user = userStorage.create(user);
        User user1 = userStorage.getById(user.getId());

        assertThat(user.getId().equals(user1.getId()));
        assertThat(user.getEmail().equals(user1.getEmail()));
        assertThat(user.getLogin().equals(user1.getLogin()));
    }

    @Test
    void testDeleteUser() {
        User user = User.builder()
                .email("delete_user")
                .login("login-delete-user")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        user = userStorage.create(user);

        Collection<User> users = userStorage.getAll();
        assertThat(users).hasSize(7);

        userStorage.deleteById(user.getId());

        users = userStorage.getAll();
        assertThat(users).hasSize(6);
    }

    @Test
    void testFindAll() {
        Collection<User> users = userStorage.getAll();
        assertThat(users).hasSize(3);
    }

    @Test
    void testUpdateUser() {
        User user = userStorage.getById(1L);
        assertThat(user.equals(null));

        User user2 = user;
        user.setName("Update Name");

        userStorage.update(user);

        User chek = userStorage.getById(1L);

        assertThat(!chek.equals(null));
        assertThat(user.equals(user2));
    }

    @Test
    void testSaveFriend() {
        Collection<User> friends = userStorage.getUserFriends(1L);
        assertThat(friends).hasSize(0);

        User user = User.builder()
                .email("email@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);

        userStorage.addFriend(1L, 2L);

        friends = userStorage.getUserFriends(1L);
        assertThat(friends).hasSize(1);
    }

    @Test
    void testDeleteFriend() {
        User user = User.builder()
                .email("email@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);

        userStorage.addFriend(1L, 2L);
        Collection<User> friends = userStorage.getUserFriends(1L);

        assertThat(friends).hasSize(1);

        userStorage.removeFriend(1L, 2L);

        friends = userStorage.getUserFriends(1L);

        assertThat(friends).hasSize(0);
    }

    @Test
    void testGetFriends() {
        Collection<User> friends = userStorage.getUserFriends(1L);
        assertThat(friends).hasSize(0);
    }

    @Test
    void testFindCommonFriends() {
        userStorage.addFriend(1L, 2L);
        User user = User.builder()
                .email("email@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        userStorage.create(user);

        userStorage.addFriend(1L, 3L);
        userStorage.addFriend(2L, 3L);

        Collection<User> friendsUser2 = userStorage.getUserFriends(2L);
        Collection<User> commonFriends = userStorage.getMutualFriends(1L, 2L);

        assertThat(commonFriends).hasSize(1);
        assertThat(commonFriends).isEqualTo(friendsUser2);
    }

}