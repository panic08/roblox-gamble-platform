package ru.marthastudios.robloxcasino.dto;

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
@Schema(name = "IncomingWithdrawal")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IncomingWithdrawalDto {
    private UserDto user;
    private List<UserItemDto> userItems;
}
