package ru.marthastudios.robloxcasino.payload.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "CreateUpgraderItem")
public class CreateUpgraderItemRequest {
    @JsonProperty("user_item_id")
    private long userItemId;
}
