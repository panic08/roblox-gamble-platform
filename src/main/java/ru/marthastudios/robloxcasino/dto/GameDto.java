package ru.marthastudios.robloxcasino.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.marthastudios.robloxcasino.enums.GameType;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "Game")
public class GameDto {
    private long id;
    private GameType type;
    private int amount;
    private boolean isWin;
    private long createdAt;

    public void setIsWin(boolean isWin){
        this.isWin = isWin;
    }
}
