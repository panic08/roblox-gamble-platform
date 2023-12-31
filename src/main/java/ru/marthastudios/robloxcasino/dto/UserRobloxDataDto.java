package ru.marthastudios.robloxcasino.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "UserRobloxData")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRobloxDataDto {
    @JsonIgnore
    private long id;
    private long robloxId;
    private String robloxNickname;
    private String robloxAvatarLink;
}
