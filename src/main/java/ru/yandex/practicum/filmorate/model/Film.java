package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@SuperBuilder
@NoArgsConstructor(force = true)
public class Film extends BaseUnit {

    @NotBlank(message = "Введите название фильма.")
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    @NotNull
    private Mpa mpa;

    private Set<Genre> genres = new LinkedHashSet<>();
}