package ru.marthastudios.robloxcasino.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.marthastudios.robloxcasino.dto.UserDto;
import ru.marthastudios.robloxcasino.dto.UserItemDto;
import ru.marthastudios.robloxcasino.mapper.UserToUserDtoMapperImpl;
import ru.marthastudios.robloxcasino.model.Item;
import ru.marthastudios.robloxcasino.model.UserItem;
import ru.marthastudios.robloxcasino.repository.ItemRepository;
import ru.marthastudios.robloxcasino.repository.UserItemRepository;
import ru.marthastudios.robloxcasino.repository.UserRepository;
import ru.marthastudios.robloxcasino.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserToUserDtoMapperImpl userToUserDtoMapper;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final ItemRepository itemRepository;

    @Override
    public UserDto getAuthInfo(long principalId) {
        return userToUserDtoMapper.userToUserDto(userRepository.findById(principalId).orElse(null));
    }

    @Override
    public List<UserItemDto> getAllItem(long id, Integer minIndex, Integer maxIndex) {
        if (maxIndex == null){
            maxIndex = 9999999;
        }
        if (minIndex == null){
            minIndex = 0;
        }

        List<UserItem> userItems = userItemRepository.findAllByUserIdWithOffsetAndLimit(id, minIndex, maxIndex);

        List<Long> itemIds = new ArrayList<>();

        for (UserItem userItem : userItems){
            itemIds.add(userItem.getItemId());
        }

        Iterable<Item> items = itemRepository.findAllById(itemIds);

        List<UserItemDto> userItemDtoList = new ArrayList<>();

        for (UserItem userItem : userItems){
            for (Item item : items){
                if (userItem.getItemId().equals(item.getId())){
                    userItemDtoList.add(UserItemDto.builder().id(userItem.getId())
                            .itemId(item.getId()).itemFullName(item.getFullName())
                            .itemCost(item.getCost()).build());
                }
            }
        }

        return userItemDtoList;
    }
}
