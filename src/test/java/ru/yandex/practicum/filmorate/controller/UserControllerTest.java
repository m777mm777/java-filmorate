package ru.yandex.practicum.filmorate.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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
   //     userController = new UserController(new UserService());
    }

    @Test
    void getAllUserAndChekChangeNameToLogin() {
        User user = new User();
        user.setEmail("ere@eetr.ru");
        user.setLogin("logiin");
        user.setName("  ");
        user.setBirthday(LocalDate.of(1914, 7, 28));

        userController.create(user);

        List<User> collectionUser = new ArrayList<>();
        collectionUser.add(user);

        Assertions.assertEquals(userController.getAll(),collectionUser);
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setEmail("ere@eetr.ru");
        user.setLogin("logiin");
        user.setName("  ");
        user.setBirthday(LocalDate.of(1914, 7, 28));

        userController.create(user);

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("ere@eetr.ru");
        user2.setLogin("logiin");
        user2.setName("logiin");
        user2.setBirthday(LocalDate.of(1914, 7, 28));

        userController.update(user2);

        List<User> collectionUser = new ArrayList<>();
        collectionUser.add(user2);

        Assertions.assertEquals(userController.getAll(),collectionUser);
    }
}
