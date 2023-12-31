package ru.marthastudios.robloxcasino.api.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetUsersByUsernameRequest {
    private String[] usernames;
    @JsonProperty("excludeBannedUsers")
    private boolean isExcludeBannedUsers;
}
