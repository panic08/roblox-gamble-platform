package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.GameAlreadyStartedException;

@RestControllerAdvice
public class GameAlreadyStartedAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(GameAlreadyStartedException.class)
    public ErrorDto handleGameAlreadyStartedException(GameAlreadyStartedException gameAlreadyStartedException) {
        return new ErrorDto(gameAlreadyStartedException.getMessage());
    }
}
