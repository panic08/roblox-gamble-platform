package ru.marthastudios.robloxcasino.payload.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Schema(name = "CreateWithdrawal")
public class CreateWithdrawalRequest {
    @NotEmpty(message = "Specify at least 1 user_item_id")
    @JsonProperty("user_items_ids")
    private Set<Long> userItemsIds;
}
