package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@SuppressWarnings("checkstyle:WhitespaceAround")
@Data
@SuperBuilder
public class User extends BaseUnit {

    @Email
    @NotEmpty
    private String email;

    @NotBlank
    private String login;

    //Может быть пустым тогда использовать логин сделать проверку через ввалидацию собственная анотация или в коде
    private String name;

    @NonNull
    @PastOrPresent
    private LocalDate birthday;
}