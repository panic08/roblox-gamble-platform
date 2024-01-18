package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.AccountsSamesException;

@RestControllerAdvice
public class AccountsSamesAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AccountsSamesException.class)
    public ErrorDto handleAccountsSamesException(AccountsSamesException accountsSamesException){
        return new ErrorDto(accountsSamesException.getMessage());
    }
}
