package ru.marthastudios.robloxcasino.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.marthastudios.robloxcasino.dto.ItemDto;
import ru.marthastudios.robloxcasino.mapper.ItemToItemDtoMapperImpl;
import ru.marthastudios.robloxcasino.repository.ItemRepository;
import ru.marthastudios.robloxcasino.service.ItemService;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemToItemDtoMapperImpl itemToItemDtoMapper;
    @Value("${path.media}")
    private String pathToMedia;

    @Override
    public ItemDto getById(long id) {
        return itemToItemDtoMapper.itemToItemDto(itemRepository.findById(id).orElse(null));
    }

    @Override
    public ResponseEntity<Resource> getImageById(long id) {
        Path path = Paths.get(pathToMedia + id + ".webp");
        Resource resource = new FileSystemResource(path);

        if (resource.exists() && resource.isReadable()){
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
