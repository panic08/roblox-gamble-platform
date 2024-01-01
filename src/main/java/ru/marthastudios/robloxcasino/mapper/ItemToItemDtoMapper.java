package ru.marthastudios.robloxcasino.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.marthastudios.robloxcasino.dto.ItemDto;
import ru.marthastudios.robloxcasino.model.Item;

@Mapper(componentModel = "spring")
public interface ItemToItemDtoMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "fullName", source = "fullName"),
            @Mapping(target = "cost", source = "cost")
    })
    ItemDto itemToItemDto(Item item);
}
