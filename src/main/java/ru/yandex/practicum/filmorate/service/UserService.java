package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public void addFriend(Long id, Long friendId) {
        User user = getById(id);
        User friend = getById(friendId);
        friendsStorage.addFriend(user.getId(), friend.getId());
    }

    public void removeFriend(Long id, Long friendId) {
        User user = getById(id);
        User friend = getById(friendId);
        friendsStorage.removeFriend(user.getId(), friend.getId());
    }

    public List<User> getFriends(Long idUser) {
        User user = getById(idUser);
        return friendsStorage.getUserFriends(user.getId());
    }

    public List<User> getMutualFriends(Long idUser1, Long idUser2) {
        User user = getById(idUser1);
        User otherUser = getById(idUser2);
        return friendsStorage.getMutualFriends(user.getId(), otherUser.getId());
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        getById(user.getId());
        validate(user);
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    public void validate(User user) {
        if (isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
    }
}