package ru.marthastudios.robloxcasino.api.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUsersByUsernameResponse {
    private Data[] data;
    @Getter
    @Setter
    public static class Data{
        private String requestedUsername;
        @JsonProperty("hasVerifiedBadge")
        private boolean isHasVerifiedBadge;
        private long id;
        private String name;
        private String displayName;
    }
}
