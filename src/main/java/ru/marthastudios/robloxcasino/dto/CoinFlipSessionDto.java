package ru.marthastudios.robloxcasino.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "CoinFlipSession")
public class CoinFlipSessionDto {
    private long id;
    private UserDto issuerUser;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto otherSideUser;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto winnerUser;
    private List<ItemDto> issuerItems;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ItemDto> otherSideItems;
    private String serverSeed;
    private String clientSeed;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String salt;
    private long createdAt;
}
