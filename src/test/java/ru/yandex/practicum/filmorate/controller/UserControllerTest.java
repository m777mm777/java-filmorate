package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUP() {
        userController = new UserController();
    }

    @Test
    void getAllUserAndChekChangeNameToLogin() {
        User user = User.builder()
                .email("ere@eetr.ru")
                .login("logiin")
                .name("  ")
                .birthday(LocalDate.of(1989,10,28))
                .build();

        userController.create(user);

        User user2 = User.builder()
                .email("ere@eetr.ru")
                .login("logiin")
                .name("logiin")
                .birthday(LocalDate.of(1989,10,28))
                .build();

        List<User> collectionUser = new ArrayList<>();
        collectionUser.add(user2);

        Assertions.assertEquals(userController.getAll(),collectionUser);
    }

    @Test
    void updateUser() {
        User user = User.builder()
                .email("ere@eetr.ru")
                .login("logiin")
                .name("  ")
                .birthday(LocalDate.of(1989,10,28))
                .build();

        userController.create(user);

        User user2 = User.builder()
                .id(1L)
                .email("ere@eetr.ru")
                .login("NoTlogiin")
                .name("NotLogin")
                .birthday(LocalDate.of(1989,10,28))
                .build();

        userController.update(user2);

        List<User> collectionUser = new ArrayList<>();
        collectionUser.add(user2);

        Assertions.assertEquals(userController.getAll(),collectionUser);
    }
}
