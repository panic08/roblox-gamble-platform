package ru.marthastudios.robloxcasino.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import ru.marthastudios.robloxcasino.dto.ItemDto;

public interface ItemService {
    ItemDto getById(long id);
    ResponseEntity<Resource> getImageById(long id);
}
