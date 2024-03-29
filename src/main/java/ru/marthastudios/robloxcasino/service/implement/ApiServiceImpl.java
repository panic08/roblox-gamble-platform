package ru.marthastudios.robloxcasino.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.marthastudios.robloxcasino.dto.UserItemDto;
import ru.marthastudios.robloxcasino.model.Item;
import ru.marthastudios.robloxcasino.model.UserItem;
import ru.marthastudios.robloxcasino.payload.user.CreateUserItemsRequest;
import ru.marthastudios.robloxcasino.repository.ItemRepository;
import ru.marthastudios.robloxcasino.repository.UserItemRepository;
import ru.marthastudios.robloxcasino.service.ApiService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {
    private final UserItemRepository userItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public List<UserItemDto> createUserItems(long userId, CreateUserItemsRequest createUserItemsRequest) {
        List<Item> items = new ArrayList<>();

        for (long itemId : createUserItemsRequest.getItemsIds()) {
            items.add(itemRepository.findById(itemId).orElse(null));
        }

        List<UserItem> userItems = new ArrayList<>();
        List<UserItemDto> userItemDtos = new ArrayList<>();

        for (Item item : items) {
            userItems.add(UserItem.builder()
                            .userId(userId)
                            .itemId(item.getId())
                    .build());

            userItemDtos.add(UserItemDto.builder()
                    .itemId(item.getId())
                    .itemCost(item.getCost())
                    .itemFullName(item.getFullName())
                    .build());
        }

        userItems = (List<UserItem>) userItemRepository.saveAll(userItems);

        for (int i = 0; i < userItemDtos.size(); i++){
            userItemDtos.get(i).setId(userItems.get(i).getId());
        }

        return userItemDtos;
    }

    @Override
    public void deleteUserItem(long userId, long userItemId) {
        userItemRepository.deleteById(userItemId);
    }
}
