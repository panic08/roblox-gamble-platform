package ru.marthastudios.robloxcasino.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Schema(name = "UpgraderItem")
public class UpgraderItemDto {
    private long id;
    private long itemId;
    private String itemFullName;
    private int itemCost;
}
