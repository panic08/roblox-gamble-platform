package ru.marthastudios.robloxcasino.api.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDetailedUserInformationResponse {
    private String description;
    private String created;
    private boolean isBanned;
    private String externalAppDisplayName;
    @JsonProperty("hasVerifiedBadge")
    private Boolean isHasVerifiedBadge;
    private long id;
    private String name;
    private String displayName;
}
