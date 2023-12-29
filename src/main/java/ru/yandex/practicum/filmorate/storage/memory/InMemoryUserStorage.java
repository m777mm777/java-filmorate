package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage extends InMemoryBaseStorage<User> implements UserStorage {

    public void addFriend(Long idUser1, Long idUser2) {
        if (getById(idUser1) != null && getById(idUser2)  != null) {
            getById(idUser1).getFriends().add(idUser2);
            getById(idUser2).getFriends().add(idUser1);
        } else {
            throw new DataNotFoundException("Пользователь не найден");
        }
    }

    public void removeFriend(Long idUser1, Long idUser2) {
        if (getById(idUser1) != null && getById(idUser2)  != null) {
            getById(idUser1).getFriends().remove(idUser2);
            getById(idUser2).getFriends().remove(idUser1);
        } else {
            throw new DataNotFoundException("Пользователь не найден");
        }
    }

    public List<User> getUserFriends(Long idUser) {
        User user = getById(idUser);
        List<User> friends = new ArrayList<>();

        if (user == null) {
            throw new DataNotFoundException("Пользователь не найден");
        }
        for (Long i: user.getFriends()) {
            friends.add(getById(i));
        }
        return friends;
    }

    public List<User> getMutualFriends(Long idUser1, Long idUser2) {
        if (getById(idUser1) == null && getById(idUser2)  == null) {
            throw new DataNotFoundException("Пользователь не найден");
        }

        List<User> friendsByUser1 = getUserFriends(idUser1);
        List<User> friendsByUser2 = getUserFriends(idUser2);

        return friendsByUser1.stream()
                .filter(friendsByUser2::contains)
                .collect(Collectors.toList());
    }

    @Override
    public void validate(User data) {
        if (isBlank(data.getName())) {
            data.setName(data.getLogin());
        }
    }

}
