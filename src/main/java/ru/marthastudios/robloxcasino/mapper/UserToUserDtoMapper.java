package ru.marthastudios.robloxcasino.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.marthastudios.robloxcasino.dto.UserDto;
import ru.marthastudios.robloxcasino.model.User;

@Mapper(componentModel = "spring")
public interface UserToUserDtoMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "role", source = "role"),
            @Mapping(target = "robloxData", source = "robloxData"),
            @Mapping(target = "registeredAt", source = "registeredAt")
    })
    UserDto userToUserDto(User user);
}
