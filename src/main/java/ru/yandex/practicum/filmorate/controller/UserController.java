package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.FilmorateValidationExeption;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    @SuppressWarnings("checkstyle:ModifierOrder")
    private final static LocalDate DATA_NOW = LocalDate.now();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user {}", user);
        return super.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Update user {}", user);
        return super.update(user);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Get user {}");
        return super.getAll();
    }

    @Override
    public void validate(User data) {
        if (data.getBirthday().isAfter(DATA_NOW)) {
            throw new FilmorateValidationExeption("User birthday data is invalid");
        }
        if (Objects.equals(data.getName(), null)) {
            data.setName(data.getLogin());
        }
    }
}
