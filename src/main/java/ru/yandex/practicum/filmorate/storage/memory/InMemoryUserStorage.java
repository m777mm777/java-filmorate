package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Component
public class InMemoryUserStorage extends InMemoryBaseStorage<User> implements UserStorage {

    public void addFriend(Long idUser1, Long idUser2) {
            getById(idUser1).getFriends().add(getById(idUser2).getId());
            getById(idUser2).getFriends().add(getById(idUser1).getId());
    }

    public void removeFriend(Long idUser1, Long idUser2) {
            getById(idUser1).getFriends().remove(getById(idUser2).getId());
            getById(idUser2).getFriends().remove(getById(idUser1).getId());
    }

    public List<User> getUserFriends(Long idUser) {
       return getById(idUser).getFriends().stream().map((Long id1) -> getById(id1)).collect(Collectors.toList());

    }

    public List<User> getMutualFriends(Long idUser1, Long idUser2) {
        return getById(idUser1).getFriends().stream()
                .filter(getById(idUser2).getFriends()::contains)
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