package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage extends InMemoryBaseStorage<User> implements UserStorage {

    public void addFriend(Long idUser1, Long idUser2) {
        User user = checkingAvailability(idUser1,"Пользователь 1 не найден");
        User user2 = checkingAvailability(idUser2,"Пользователь 2 не найден");
            user.getFriends().add(user2.getId());
            user2.getFriends().add((user.getId()));

    }

    public void removeFriend(Long idUser1, Long idUser2) {
        User user = checkingAvailability(idUser1,"Пользователь 1 не найден");
        User user2 = checkingAvailability(idUser2,"Пользователь 2 не найден");
        user.getFriends().remove(idUser2);
        user2.getFriends().remove(idUser1);

    }

    public List<User> getUserFriends(Long idUser) {
        User user = checkingAvailability(idUser,"Пользователь не найден");
       return user.getFriends().stream().map((Long id1) -> getById(id1)).collect(Collectors.toList());

    }

    public List<User> getMutualFriends(Long idUser1, Long idUser2) {
        User user = checkingAvailability(idUser1,"Пользователь 1 не найден");
        User user2 = checkingAvailability(idUser2,"Пользователь 2 не найден");

        return user.getFriends().stream()
                .filter(user2.getFriends()::contains)
                .map((Long id) -> getById(id))
                .collect(Collectors.toList());
    }

    @Override
    public void validate(User data) {
        if (isBlank(data.getName())) {
            data.setName(data.getLogin());
        }
    }

}
