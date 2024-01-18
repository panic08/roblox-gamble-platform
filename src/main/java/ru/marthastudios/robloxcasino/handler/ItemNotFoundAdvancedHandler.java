package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.ItemNotFoundException;

@RestControllerAdvice
public class ItemNotFoundAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ItemNotFoundException.class)
    public ErrorDto handleItemNotFoundException(ItemNotFoundException itemNotFoundException){
        return new ErrorDto(itemNotFoundException.getMessage());
    }
}
