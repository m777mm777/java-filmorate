package ru.yandex.practicum.filmorate.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String className;
    private String message;

}
