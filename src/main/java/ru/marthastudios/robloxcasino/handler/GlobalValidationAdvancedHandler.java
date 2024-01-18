package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;

@RestControllerAdvice
public class GlobalValidationAdvancedHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleException(MethodArgumentNotValidException e) {
        return processErrors(e);
    }

    private ErrorDto processErrors(MethodArgumentNotValidException e) {
        return new ErrorDto(e.getBindingResult().getFieldError().getDefaultMessage());
    }
}
