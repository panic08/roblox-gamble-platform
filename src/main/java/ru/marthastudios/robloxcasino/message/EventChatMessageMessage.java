package ru.marthastudios.robloxcasino.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.marthastudios.robloxcasino.dto.UserDto;

@Getter
@Setter
@Builder
public class EventChatMessageMessage {
    private String type;
    private Data data;

    @Getter
    @Setter
    @Builder
    public static class Data {
        private UserDto user;
        private String message;
    }
}
