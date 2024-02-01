package ru.marthastudios.robloxcasino.payload.games;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(name = "GetGameStat")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetGameStatisticResponse {
    private int totalProfit;
    private int gamesPlayed;
}
