package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("checkstyle:WhitespaceAround")
@Data
public class User extends BaseUnit {

    @Email
    @NotEmpty
    private String email;

    @NotBlank
    private String login;

    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}