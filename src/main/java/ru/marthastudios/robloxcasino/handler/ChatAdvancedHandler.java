package ru.marthastudios.robloxcasino.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.exception.ChatException;

@RestControllerAdvice
public class ChatAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ChatException.class)
    public ErrorDto handleChatException(ChatException chatException) {
        return ErrorDto.builder().message(chatException.getMessage()).build();
    }
}
