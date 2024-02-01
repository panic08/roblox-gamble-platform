package ru.marthastudios.robloxcasino.payload.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpgraderRequest {
    @JsonProperty("from_user_item_id")
    private long fromUserItemId;
    @JsonProperty("to_upgrader_item_id")
    private long toUpgraderItemId;
}
