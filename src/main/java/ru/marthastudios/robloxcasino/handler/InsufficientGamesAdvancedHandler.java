package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.InsufficientGamesException;

@RestControllerAdvice
public class InsufficientGamesAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InsufficientGamesException.class)
    public ErrorDto handleInsufficientGamesException(InsufficientGamesException insufficientGamesException){
        return new ErrorDto(insufficientGamesException.getMessage());
    }
}
