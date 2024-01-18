package ru.marthastudios.robloxcasino.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(name = "CreateUserItem")
public class CreateUserItemsRequest {
    @JsonProperty("item_ids")
    private List<Long> itemIds;
}
