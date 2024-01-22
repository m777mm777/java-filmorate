package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Optional<Long> id, Optional<Long> friendId) {
        userStorage.addFriend(checkId(id), checkId(friendId));
    }

    public void removeFriend(Optional<Long> idUser1, Optional<Long> idUser2) {
        userStorage.removeFriend(checkId(idUser1), checkId(idUser2));
    }

    public List<User> getFriends(Optional<Long> idUser) {
        return userStorage.getUserFriends(checkId(idUser));
    }

    public List<User> getMutualFriends(Optional<Long> idUser1, Optional<Long> idUser2) {
        return userStorage.getMutualFriends(checkId(idUser1), checkId(idUser2));
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Optional<Long> id) {
        return userStorage.getById(checkId(id));
    }

    public void deleteById(Optional<Long> id) {
        userStorage.deleteById(checkId(id));
    }

    private Long checkId(Optional<Long> id) {
        return id.orElseThrow(() -> new DataNotFoundException("Не верный id пользователя"));
    }

}