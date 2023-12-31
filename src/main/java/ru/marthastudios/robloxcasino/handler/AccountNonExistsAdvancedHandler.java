package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.AccountNonExistsException;

@RestControllerAdvice
public class AccountNonExistsAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNonExistsException.class)
    public ErrorDto handleAccountNonExistsException(AccountNonExistsException accountNonExistsException){
        return new ErrorDto(accountNonExistsException.getMessage());
    }

}
