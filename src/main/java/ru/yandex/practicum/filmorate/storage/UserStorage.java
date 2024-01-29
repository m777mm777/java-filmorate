package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage extends AbstractStorage<User> {
    User create(User data);

    User update(User data);

    User getById(Long id);
}
