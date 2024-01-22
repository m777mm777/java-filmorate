package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Update user {}", user);
        return userService.update(user);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Get user {}");
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Optional<Long> id) {
        log.info("Delete film {}");
        userService.deleteById(id);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Optional<Long> id) {
        log.info("Get film {}");
        return userService.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Optional<Long> id, @PathVariable Optional<Long> friendId) {
        log.info("Put friends {}");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Optional<Long> id,@PathVariable Optional<Long> friendId) {
        log.info("Delet friends {}");
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Optional<Long> id) {
        log.info("Get friends {}");
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Optional<Long> id, @PathVariable Optional<Long> otherId) {
        log.info("Get friends Mutual {}");
        return userService.getMutualFriends(id, otherId);
    }
}