package ru.marthastudios.robloxcasino.payload.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "CreateChatMessage")
public class CreateMessageRequest {
    @NotBlank(message = "Message not may be blank")
    @Size(min = 1, max = 100, message = "A message can have a minimum of 1 character and a maximum of 100 characters")
    private String message;
}
