package ru.marthastudios.robloxcasino.payload.games;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.marthastudios.robloxcasino.dto.UserItemDto;

@Getter
@Setter
@Builder
public class CreateUpgraderResponse {
    @JsonProperty("win")
    private boolean isWin;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserItemDto winUserItem;
}
