package ru.marthastudios.robloxcasino.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Item")
public class ItemDto {
    private long id;
    @JsonProperty("full_name")
    private String fullName;
    private int cost;
}
