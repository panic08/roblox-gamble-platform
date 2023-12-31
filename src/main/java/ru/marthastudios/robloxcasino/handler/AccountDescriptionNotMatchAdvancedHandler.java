package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.AccountDescriptionNotMatchException;

@RestControllerAdvice
public class AccountDescriptionNotMatchAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AccountDescriptionNotMatchException.class)
    public ErrorDto handleAccountDescriptionNotMatchException(AccountDescriptionNotMatchException accountDescriptionNotMatchException){
        return new ErrorDto(accountDescriptionNotMatchException.getMessage());
    }
}
