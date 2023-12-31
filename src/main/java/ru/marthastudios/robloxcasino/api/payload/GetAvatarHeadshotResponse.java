package ru.marthastudios.robloxcasino.api.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAvatarHeadshotResponse {
    private Data[] data;
    @Getter
    @Setter
    public static class Data{
        private long targetId;
        private String state;
        private String imageUrl;
        private String version;
    }
}
