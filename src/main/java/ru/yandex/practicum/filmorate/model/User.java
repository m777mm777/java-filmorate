package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@SuppressWarnings("checkstyle:WhitespaceAround")
@Data
public class User extends BaseUnit {

    @Email
    @NotEmpty
    private String email;

    @NotBlank
    private String login;

    //Может быть пустым тогда использовать логин сделать проверку через ввалидацию собственная анотация или в коде
    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}