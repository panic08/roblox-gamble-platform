package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.IncorrectCostException;

@RestControllerAdvice
public class IncorrectCostAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IncorrectCostException.class)
    public ErrorDto handleIncorrectCostException(IncorrectCostException incorrectCostException){
        return new ErrorDto(incorrectCostException.getMessage());
    }
}
