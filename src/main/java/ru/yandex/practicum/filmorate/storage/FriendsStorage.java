package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {

    public void addFriend(Long id, Long friendId);

    public void removeFriend(Long id, Long friendId);

    public List<User> getUserFriends(Long id);

    public List<User> getMutualFriends(Long id, Long otherId);
}
