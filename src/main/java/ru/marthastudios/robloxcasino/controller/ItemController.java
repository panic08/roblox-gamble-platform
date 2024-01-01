package ru.marthastudios.robloxcasino.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.marthastudios.robloxcasino.dto.ItemDto;
import ru.marthastudios.robloxcasino.service.implement.ItemServiceImpl;


@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
@Tag(name = "Items", description = "This component describes the Items API")
public class ItemController {
    private final ItemServiceImpl itemService;
    @GetMapping("/{id}")
    @Operation(description = "Get Item by id")
    @ApiResponse(responseCode = "200", description = "We got item by id",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ItemDto.class))})
    public ItemDto getById(@PathVariable("id") long id){
        return itemService.getById(id);
    }

    @GetMapping("/{id}/media")
    @Operation(description = "Get image of Item by id")
    @ApiResponse(responseCode = "200", description = "We got image of Item by id",
            content = {@Content(mediaType = "image/png")})
    @ApiResponse(responseCode = "404", description = "There is no such thing as an Item with that id")
    public ResponseEntity<Resource> getImageById(@PathVariable("id") long id){
        return itemService.getImageById(id);
    }
}
