package ru.marthastudios.robloxcasino.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.marthastudios.robloxcasino.enums.UserRole;

@Data
@Schema(name = "User")
public class UserDto {
    private long id;
    private UserRole role;
    @JsonProperty("roblox_data")
    private UserRobloxDataDto robloxData;
    @JsonProperty("registered_at")
    private long registeredAt;
}
