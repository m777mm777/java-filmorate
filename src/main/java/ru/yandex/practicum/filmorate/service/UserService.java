package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long id, Long friendId) {
        userStorage.addFriend(id, friendId);
    }

    public void removeFriend(Long idUser1, Long idUser2) {
        userStorage.removeFriend(idUser1, idUser2);
    }

    public List<User> getFriends(Long idUser) {
        return userStorage.getUserFriends(idUser);
    }

    public List<User> getMutualFriends(Long idUser1, Long idUser2) {
        return userStorage.getMutualFriends(idUser1, idUser2);
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

    public User getById(Long id) {
        return userStorage.getById(checkIdUser(id));
    }

    public void deleteById(Long id) {
        userStorage.deleteById(checkIdUser(id));
    }

    private Long checkIdUser(Long id) {
        return Optional.of(id).orElseThrow(() -> new DataNotFoundException("Не верный id пользователя"));
    }

}
