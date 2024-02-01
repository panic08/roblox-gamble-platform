package ru.marthastudios.robloxcasino.payload.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(name = "CreateUserItem")
public class CreateUserItemsRequest {
    @JsonProperty("items_ids")
    private List<Long> itemsIds;
}
