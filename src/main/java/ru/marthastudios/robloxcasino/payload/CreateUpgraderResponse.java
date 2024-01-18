package ru.marthastudios.robloxcasino.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUpgraderResponse {
    @JsonProperty("win")
    private boolean isWin;
}
