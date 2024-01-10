package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exeption.*;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(basePackages = "ru.yandex.practicum.filmorate")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class, DataIsNotValid.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notValid(ConstraintViolationException e) {
        log.debug("Получен статус 400 Not valid {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFound(DataNotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unknownException(Throwable e) {
        log.debug("Получен статус 500 {}", e.getMessage());
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }

}
