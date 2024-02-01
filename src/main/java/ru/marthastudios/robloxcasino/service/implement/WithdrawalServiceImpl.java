package ru.marthastudios.robloxcasino.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.marthastudios.robloxcasino.dto.UserItemDto;
import ru.marthastudios.robloxcasino.exception.ItemNotFoundException;
import ru.marthastudios.robloxcasino.mapper.UserToUserDtoMapperImpl;
import ru.marthastudios.robloxcasino.model.Item;
import ru.marthastudios.robloxcasino.model.UserItem;
import ru.marthastudios.robloxcasino.payload.user.CreateWithdrawalRequest;
import ru.marthastudios.robloxcasino.dto.IncomingWithdrawalDto;
import ru.marthastudios.robloxcasino.repository.ItemRepository;
import ru.marthastudios.robloxcasino.repository.UserItemRepository;
import ru.marthastudios.robloxcasino.repository.UserRepository;
import ru.marthastudios.robloxcasino.service.WithdrawalService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final ItemRepository itemRepository;
    private static final List<IncomingWithdrawalDto> incomingWithdrawalList = new ArrayList<>();
    private final UserToUserDtoMapperImpl userToUserDtoMapper;

    @Transactional
    @Override
    public void create(long principalId, CreateWithdrawalRequest createWithdrawalRequest) {
        List<UserItem> userItems = userItemRepository.findAllByUserId(principalId);

        if (userItems.isEmpty()){
            throw new ItemNotFoundException("The user does not have such an item");
        }

        List<UserItem> userItemsForDelete = new ArrayList<>();

        for (long userItemId : createWithdrawalRequest.getUserItemsIds()){
            boolean isExists = false;

            for (UserItem userItem : userItems){
                if (userItem.getId().equals(userItemId)){
                    isExists = true;

                    userItemsForDelete.add(userItem);
                    break;
                }
            }

            if (!isExists){
                throw new ItemNotFoundException("The user does not have such an item");
            }
        }

        userItemRepository.deleteAll(userItemsForDelete);

        List<UserItemDto> userDtos = new ArrayList<>();

        for (UserItem userItem : userItemsForDelete) {
            Item item = itemRepository.findById(userItem.getItemId()).orElse(null);

            userDtos.add(UserItemDto.builder()
                    .id(userItem.getId())
                    .itemId(item.getId())
                    .itemFullName(item.getFullName())
                    .itemCost(item.getCost())
                    .build());
        }

        incomingWithdrawalList.add(IncomingWithdrawalDto.builder()
                .user(userToUserDtoMapper.userToUserDto(userRepository.findById(principalId).orElse(null)))
                .userItems(userDtos)
                .build());

    }

    @Override
    public List<IncomingWithdrawalDto> getAllIncoming() {
        List<IncomingWithdrawalDto> incomingWithdrawals = new ArrayList<>(incomingWithdrawalList);

        incomingWithdrawalList.clear();

        return incomingWithdrawals;
    }
}
