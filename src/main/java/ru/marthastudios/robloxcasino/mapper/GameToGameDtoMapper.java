package ru.marthastudios.robloxcasino.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.marthastudios.robloxcasino.dto.GameDto;
import ru.marthastudios.robloxcasino.model.Game;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameToGameDtoMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "type", source = "type"),
            @Mapping(target = "amount", source = "amount"),
            @Mapping(target = "isWin", source = "isWin"),
            @Mapping(target = "createdAt", source = "createdAt"),
    })
    List<GameDto> gameListToGameDtoList(List<Game> gameList);
}
