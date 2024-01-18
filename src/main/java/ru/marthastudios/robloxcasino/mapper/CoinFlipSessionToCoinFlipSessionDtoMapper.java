package ru.marthastudios.robloxcasino.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.marthastudios.robloxcasino.dto.CoinFlipSessionDto;
import ru.marthastudios.robloxcasino.model.CoinFlipSession;

@Mapper(componentModel = "spring")
public interface CoinFlipSessionToCoinFlipSessionDtoMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "issuerUser", ignore = true),
            @Mapping(target = "otherSideUser", ignore = true),
            @Mapping(target = "issuerItems", ignore = true),
            @Mapping(target = "otherSideItems", ignore = true),
            @Mapping(target = "serverSeed", source = "serverSeed"),
            @Mapping(target = "clientSeed", source = "clientSeed"),
            @Mapping(target = "salt", source = "salt"),
            @Mapping(target = "winnerUser", ignore = true),
            @Mapping(target = "createdAt", source = "createdAt"),
    })
    CoinFlipSessionDto coinFlipSessionToCoinFlipSessionDto(CoinFlipSession coinFlipSession);

}
