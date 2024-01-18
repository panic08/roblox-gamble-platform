package ru.marthastudios.robloxcasino.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpgraderRequest {
    @JsonProperty("user_item_id")
    private long userItemId;
    @JsonProperty("upgrader_item_id")
    private long upgraderItemId;
}
