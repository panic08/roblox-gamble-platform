package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.InsufficientRoleException;

@RestControllerAdvice
public class InsufficientRoleAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InsufficientRoleException.class)
    public ErrorDto handleInsufficientRoleException(InsufficientRoleException insufficientRoleException) {
        return new ErrorDto(insufficientRoleException.getMessage());
    }
}
