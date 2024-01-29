package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface AbstractStorage<T> {

    List<T> getAll();

    void deleteById(Long id);

}
