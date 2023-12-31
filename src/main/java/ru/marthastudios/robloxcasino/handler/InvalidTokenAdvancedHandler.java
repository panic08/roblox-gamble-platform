package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.InvalidTokenException;

@RestControllerAdvice
public class InvalidTokenAdvancedHandler {
    @ResponseBody
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleInvalidTokenException(InvalidTokenException invalidTokenException){
        return new ErrorDto(invalidTokenException.getMessage());
    }
}
