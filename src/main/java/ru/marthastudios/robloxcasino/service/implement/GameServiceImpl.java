package ru.marthastudios.robloxcasino.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.marthastudios.robloxcasino.dto.*;
import ru.marthastudios.robloxcasino.enums.GameType;
import ru.marthastudios.robloxcasino.enums.UserRole;
import ru.marthastudios.robloxcasino.exception.*;
import ru.marthastudios.robloxcasino.mapper.*;
import ru.marthastudios.robloxcasino.message.EventCoinFlipSessionMessage;
import ru.marthastudios.robloxcasino.model.*;
import ru.marthastudios.robloxcasino.payload.games.*;
import ru.marthastudios.robloxcasino.repository.*;
import ru.marthastudios.robloxcasino.service.GameService;
import ru.marthastudios.robloxcasino.util.DiceRollUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {
    private final CoinFlipSessionRepository coinFlipSessionRepository;
    private final UserItemRepository userItemRepository;
    private final UpgraderItemRepository upgraderItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ItemToItemDtoMapperImpl itemToItemDtoMapper;
    private final CoinFlipSessionToCoinFlipSessionDtoMapperImpl coinFlipSessionToCoinFlipSessionDtoMapper;
    private final UserToUserDtoMapperImpl userToUserDtoMapper;
    @Value("${games.userBotId}")
    private Long userBotId;


    @Transactional
    @Override
    public void createCoinFlipSession(long principalId, CreateCoinFlipSessionRequest createCoinFlipSessionRequest) {
        List<UserItem> userItems = userItemRepository.findAllByUserId(principalId);

        if (userItems.isEmpty()){
            throw new ItemNotFoundException("The user does not have such an item");
        }

        List<Long> userItemIdsForDelete = new ArrayList<>();
        List<Long> itemIdsForGame = new ArrayList<>();

        for (Long userItemId : createCoinFlipSessionRequest.getUserItemsIds()){
            boolean isExists = false;

            UserItem userItemLinkForDelete = null;

            for (UserItem userItem1 : userItems){
                if (userItem1.getId().equals(userItemId)){
                    isExists = true;

                    itemIdsForGame.add(userItem1.getItemId());
                    userItemIdsForDelete.add(userItemId);

                    userItemLinkForDelete = userItem1;
                    break;
                }
            }

            userItems.remove(userItemLinkForDelete);

            if (!isExists){
                throw new ItemNotFoundException("The user does not have such an item");
            }
        }

        userItemRepository.deleteAllById(userItemIdsForDelete);

        List<CoinFlipSessionItem> coinFlipSessionItems = new ArrayList<>();

        for (Long id : itemIdsForGame){
            coinFlipSessionItems.add(CoinFlipSessionItem.builder()
                    .itemId(id).userId(principalId).build());
        }

        CoinFlipSession coinFlipSession = CoinFlipSession.builder()
                .issuerUserId(principalId)
                .items(coinFlipSessionItems)
                .serverSeed(DiceRollUtil.generateSecret())
                .clientSeed("00000000000f424043f26367f461f6947fb4b8818df0aa6d6030e26a9d9add13")
                .createdAt(System.currentTimeMillis())
                .build();

        coinFlipSessionRepository.save(coinFlipSession);

        List<Item> itemList = new ArrayList<>();

        for (Long id : itemIdsForGame){
            itemList.add(itemRepository.findById(id).orElse(null));
        }

        List<ItemDto> itemDtoList = itemToItemDtoMapper.itemListToItemDtoList(itemList);

        UserDto issuerUser = userToUserDtoMapper.userToUserDto(userRepository.findById(principalId).orElse(null));

        CoinFlipSessionDto coinFlipSessionDto = coinFlipSessionToCoinFlipSessionDtoMapper
                .coinFlipSessionToCoinFlipSessionDto(coinFlipSession);

        coinFlipSessionDto.setIssuerItems(itemDtoList);
        coinFlipSessionDto.setIssuerUser(issuerUser);

        simpMessagingTemplate.convertAndSend("/game/coinflip/topic",
                EventCoinFlipSessionMessage.builder().type("create_coinFlip_session").data(coinFlipSessionDto).build());
    }

    @Override
    public List<CoinFlipSessionDto> getAllCoinFlipSession() {
        Iterable<CoinFlipSession> coinFlipSessions = coinFlipSessionRepository.findAll();
        List<CoinFlipSessionDto> coinFlipSessionDtoList = new ArrayList<>();

        for (CoinFlipSession coinFlipSession : coinFlipSessions){
            CoinFlipSessionDto coinFlipSessionDto = coinFlipSessionToCoinFlipSessionDtoMapper
                    .coinFlipSessionToCoinFlipSessionDto(coinFlipSession);

            coinFlipSessionDto.setIssuerUser(userToUserDtoMapper.userToUserDto(
                    userRepository.findById(coinFlipSession.getIssuerUserId()).orElse(null)
            ));

            if (coinFlipSession.getWinnerUserId() != null){
                coinFlipSessionDto.setOtherSideUser(userToUserDtoMapper.userToUserDto(
                        userRepository.findById(coinFlipSession.getOtherSideUserId()).orElse(null)
                ));

                if (coinFlipSession.getWinnerUserId().equals(coinFlipSession.getIssuerUserId())){
                    coinFlipSessionDto.setWinnerUser(coinFlipSessionDto.getIssuerUser());
                } else if (coinFlipSession.getWinnerUserId().equals(coinFlipSession.getOtherSideUserId())){
                    coinFlipSessionDto.setWinnerUser(coinFlipSessionDto.getOtherSideUser());
                }
            }

            List<ItemDto> issuerItems = new ArrayList<>();
            List<ItemDto> otherSideItems = new ArrayList<>();

            for (CoinFlipSessionItem coinFlipSessionItem : coinFlipSession.getItems()){
                if (coinFlipSessionItem.getUserId().equals(coinFlipSession.getIssuerUserId())){
                    issuerItems.add(itemToItemDtoMapper.itemToItemDto(
                            itemRepository.findById(coinFlipSessionItem.getItemId()).orElse(null)
                    ));
                } else if (coinFlipSessionItem.getUserId().equals(coinFlipSession.getOtherSideUserId())){
                    otherSideItems.add(itemToItemDtoMapper.itemToItemDto(
                            itemRepository.findById(coinFlipSessionItem.getItemId()).orElse(null)
                    ));
                }
            }

            coinFlipSessionDto.setIssuerItems(issuerItems);
            coinFlipSessionDto.setOtherSideItems(otherSideItems);

            coinFlipSessionDtoList.add(coinFlipSessionDto);
        }


        return coinFlipSessionDtoList;
    }

    @Transactional
    @Override
    public void joinCoinFlipSession(long principalId, long id, JoinCoinFlipSessionRequest joinCoinFlipSessionRequest) {
        CoinFlipSession coinFlipSession = coinFlipSessionRepository.findById(id)
                .orElse(null);

        assert coinFlipSession != null;

        if (coinFlipSession.getIssuerUserId().equals(principalId)){
            throw new AccountsSamesException("You cannot log in to the CoinFlipSession you created");
        }

        if (coinFlipSession.getWinnerUserId() != null){
            throw new GameAlreadyStartedException("You cannot log in to an already completed CoinFlipSession");
        }

        List<Long> principalUserItemIdsForDelete = new ArrayList<>();
        List<Long> itemIdsForGame = new ArrayList<>();

        List<UserItem> principalItems = userItemRepository.findAllByUserId(principalId);

        for (Long userItemId : joinCoinFlipSessionRequest.getUserItemsIds()){
            boolean isExists = false;

            UserItem userItemLinkForDelete = null;

            for (UserItem userItem1 : principalItems) {
                if (userItem1.getId().equals(userItemId)) {
                    isExists = true;

                    itemIdsForGame.add(userItem1.getItemId());
                    principalUserItemIdsForDelete.add(userItemId);

                    userItemLinkForDelete = userItem1;
                    break;
                }
            }


            principalItems.remove(userItemLinkForDelete);

            if (!isExists){
                throw new ItemNotFoundException("The user does not have such an item");
            }
        }

        List<Item> issuerItemList = new ArrayList<>();

        int issuerItemsCost = 0;

        for (CoinFlipSessionItem coinFlipSessionItem : coinFlipSession.getItems()){
            Item newIssuerItem = itemRepository.findById(coinFlipSessionItem.getItemId()).orElse(null);

            issuerItemsCost += newIssuerItem.getCost();

            issuerItemList.add(newIssuerItem);
        }

        List<Item> otherSideItemList = new ArrayList<>();

        int otherSideItemsCost = 0;

        for (Long key : itemIdsForGame){
            Item newOtherSideItem = itemRepository.findById(key).orElse(null);

            otherSideItemsCost += newOtherSideItem.getCost();

            otherSideItemList.add(newOtherSideItem);
        }

        if (otherSideItemsCost < issuerItemsCost - (issuerItemsCost * 10 / 100) || otherSideItemsCost > issuerItemsCost + (issuerItemsCost * 10 / 100)){
            throw new IncorrectCostException("The sum of items did not fall within the range of the value of the sum of items issuer value");
        }

        userItemRepository.deleteAllById(principalUserItemIdsForDelete);

        List<CoinFlipSessionItem> coinFlipSessionItems = coinFlipSession.getItems();

        for (Item item : otherSideItemList){
            coinFlipSessionItems.add(CoinFlipSessionItem.builder().userId(principalId)
                    .itemId(item.getId()).build());
        }

        User issuerUser = userRepository.findById(coinFlipSession.getIssuerUserId()).orElse(null);
        User otherSideUser = userRepository.findById(principalId).orElse(null);

        coinFlipSession.setOtherSideUserId(principalId);

        String salt = DiceRollUtil.generateSecret();

        coinFlipSession.setSalt(salt);

        double luckyNumber = DiceRollUtil.generateRandomNumber(coinFlipSession.getServerSeed(),
                coinFlipSession.getClientSeed(), salt);

        List<Item> allCoinFlipSessionItems = new ArrayList<>();

        allCoinFlipSessionItems.addAll(issuerItemList);
        allCoinFlipSessionItems.addAll(otherSideItemList);

        Item itemForTakeCommission = Item.builder().cost(0).build();

        if (allCoinFlipSessionItems.size() > 2){
            for (Item item : allCoinFlipSessionItems){
                if (itemForTakeCommission.getCost() < item.getCost() && item.getCost() <= (issuerItemsCost + otherSideItemsCost) * 10 / 100){
                    itemForTakeCommission = item;
                }
            }

            if (itemForTakeCommission.getCost().equals(0)){
                for (Item item : allCoinFlipSessionItems){
                    if (itemForTakeCommission.getCost().equals(0) || itemForTakeCommission.getCost() > item.getCost()){
                        itemForTakeCommission = item;
                    }
                }
            }
        }

        boolean isTaken = false;

        if (luckyNumber <= 0.5){
            coinFlipSession.setWinnerUserId(coinFlipSession.getIssuerUserId());

            for (CoinFlipSessionItem coinFlipSessionItem : coinFlipSessionItems){
                if (!isTaken && coinFlipSessionItem.getItemId().equals(itemForTakeCommission.getId())){

                    //add item to another account-bot

                    userItemRepository.save(UserItem.builder()
                            .userId(userBotId)
                            .itemId(coinFlipSessionItem.getItemId())
                            .build());

                    isTaken = true;
                    continue;
                }

                userItemRepository.save(UserItem.builder().userId(coinFlipSession.getIssuerUserId())
                        .itemId(coinFlipSessionItem.getItemId()).build());
            }

        } else {
            coinFlipSession.setWinnerUserId(principalId);

            for (CoinFlipSessionItem coinFlipSessionItem : coinFlipSessionItems){
                if (!isTaken && coinFlipSessionItem.getItemId().equals(itemForTakeCommission.getId())){

                    //add item to another account-bot

                    userItemRepository.save(UserItem.builder()
                            .userId(userBotId)
                            .itemId(coinFlipSessionItem.getItemId())
                            .build());

                    isTaken = true;
                    continue;
                }

                userItemRepository.save(UserItem.builder().userId(principalId)
                        .itemId(coinFlipSessionItem.getItemId()).build());
            }
        }

        coinFlipSessionRepository.save(coinFlipSession);

        CoinFlipSessionDto coinFlipSessionDto = coinFlipSessionToCoinFlipSessionDtoMapper
                .coinFlipSessionToCoinFlipSessionDto(coinFlipSession);

        coinFlipSessionDto.setIssuerItems(itemToItemDtoMapper.itemListToItemDtoList(issuerItemList));
        coinFlipSessionDto.setOtherSideItems(itemToItemDtoMapper.itemListToItemDtoList(otherSideItemList));
        coinFlipSessionDto.setIssuerUser(userToUserDtoMapper.userToUserDto(issuerUser));
        coinFlipSessionDto.setOtherSideUser(userToUserDtoMapper.userToUserDto(otherSideUser));

        if (coinFlipSession.getWinnerUserId().equals(coinFlipSession.getIssuerUserId())){
            coinFlipSessionDto.setWinnerUser(coinFlipSessionDto.getIssuerUser());
        } else if (coinFlipSession.getWinnerUserId().equals(coinFlipSession.getOtherSideUserId())){
            coinFlipSessionDto.setWinnerUser(coinFlipSessionDto.getOtherSideUser());
        }

        simpMessagingTemplate.convertAndSend("/game/coinflip/topic", EventCoinFlipSessionMessage.builder()
                .type("updating_coinFlip_session")
                .data(coinFlipSessionDto).build());

        int finalIssuerItemsCost = issuerItemsCost;
        int finalOtherSideItemsCost = otherSideItemsCost;
        new Thread(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }

            coinFlipSessionRepository.deleteById(coinFlipSession.getId());

            long gameCreatedAt = System.currentTimeMillis();

            List<Game> gamesForSave = new ArrayList<>();

            Game issuerGame = Game.builder()
                    .type(GameType.COINFLIP)
                    .userId(coinFlipSession.getIssuerUserId())
                    .amount(finalIssuerItemsCost + finalOtherSideItemsCost)
                    .isWin(luckyNumber <= 0.5)
                    .createdAt(gameCreatedAt).build();

            Game otherSideGame = Game.builder()
                    .type(GameType.COINFLIP)
                    .userId(coinFlipSession.getOtherSideUserId())
                    .amount(finalIssuerItemsCost + finalOtherSideItemsCost)
                    .isWin(luckyNumber > 0.5)
                    .createdAt(gameCreatedAt).build();

            gamesForSave.add(issuerGame);
            gamesForSave.add(otherSideGame);

            gameRepository.saveAll(gamesForSave);

            simpMessagingTemplate.convertAndSend("/game/coinflip/topic", EventCoinFlipSessionMessage.builder()
                    .type("delete_coinFlip_session")
                    .data(coinFlipSessionDto).build());

        }).start();
    }

    @Transactional
    @Override
    public CreateUpgraderResponse createUpgrader(long principalId, CreateUpgraderRequest createUpgraderRequest) {
        UserItem userItem = userItemRepository.findById(createUpgraderRequest.getFromUserItemId())
                .orElse(null);

        int fromUpgraderItemCost = itemRepository.findCostById(userItem.getItemId());
        int toUpgraderItemCost = itemRepository.findCostById(
                upgraderItemRepository.findById(createUpgraderRequest.getToUpgraderItemId())
                        .orElse(null).getItemId()
        );

        if (fromUpgraderItemCost >= toUpgraderItemCost) {
            throw new IncorrectCostException("FromUserItem cannot be priced greater than or equal to ToUserItem");
        }

        userItemRepository.save(
                UserItem.builder()
                        .userId(userBotId)
                        .itemId(userItem.getItemId())
                        .build()
        );

        userItemRepository.deleteById(createUpgraderRequest.getFromUserItemId());

        double luckyNumber = DiceRollUtil.generateRandomNumber(DiceRollUtil.generateSecret(),
                "00000000000f424043f26367f461f6947fb4b8818df0aa6d6030e26a9d9add13",
                DiceRollUtil.generateSecret());

        double percentToWin = (100.0 / ((double) toUpgraderItemCost / fromUpgraderItemCost)) * 0.01;

        if (luckyNumber <= percentToWin) {
            long toItemItemId = upgraderItemRepository.findById(createUpgraderRequest.getToUpgraderItemId())
                    .orElse(null).getItemId();

            UserItem newUserItem = userItemRepository.save(UserItem.builder()
                    .userId(principalId)
                    .itemId(toItemItemId)
                    .build());

            upgraderItemRepository.deleteById(createUpgraderRequest.getToUpgraderItemId());

            Game newGame = Game.builder()
                    .type(GameType.UPGRADER)
                    .userId(principalId)
                    .isWin(true)
                    .amount(toUpgraderItemCost - fromUpgraderItemCost)
                    .createdAt(System.currentTimeMillis())
                    .build();

            gameRepository.save(newGame);

            Item newUserItemItem = itemRepository.findById(toItemItemId)
                    .orElse(null);

            UserItemDto userItemDto = UserItemDto.builder()
                    .id(newUserItem.getId())
                    .itemId(newUserItemItem.getId())
                    .itemFullName(newUserItemItem.getFullName())
                    .itemCost(newUserItemItem.getCost())
                    .build();

            return CreateUpgraderResponse.builder()
                    .isWin(true)
                    .winUserItem(userItemDto)
                    .build();
        } else {
            Game newGame = Game.builder()
                    .type(GameType.UPGRADER)
                    .userId(principalId)
                    .isWin(false)
                    .amount(fromUpgraderItemCost)
                    .createdAt(System.currentTimeMillis())
                    .build();

            gameRepository.save(newGame);

            return CreateUpgraderResponse.builder()
                    .isWin(false)
                    .build();
        }
    }

    @Override
    public List<UpgraderItemDto> getAllUpgraderItem(Integer minIndex, Integer maxIndex) {
        if (maxIndex == null){
            maxIndex = 99999999;
        }
        if (minIndex == null){
            minIndex = 0;
        }

        List<UpgraderItem> upgraderItemList =
                upgraderItemRepository.findAllWithOffsetAndLimit(minIndex, maxIndex - minIndex);

        List<UpgraderItemDto> upgraderItemDtoList = new ArrayList<>();

        Map<Long, Item> preCachedItemsMap = new HashMap<>();

        for (UpgraderItem upgraderItem : upgraderItemList) {
            Item item;

            if (preCachedItemsMap.get(upgraderItem.getItemId()) == null) {
                item = itemRepository.findById(upgraderItem.getItemId()).orElse(null);

                preCachedItemsMap.put(upgraderItem.getItemId(), item);
            } else {
                item = preCachedItemsMap.get(upgraderItem.getItemId());
            }

            upgraderItemDtoList.add(UpgraderItemDto.builder()
                    .id(upgraderItem.getId())
                    .itemId(upgraderItem.getItemId())
                    .itemFullName(item.getFullName())
                    .itemCost(item.getCost())
                    .build());
        }

        return upgraderItemDtoList;
    }

    @Transactional
    @Override
    public void createUpgraderItem(long principalId, CreateUpgraderItemRequest createUpgraderItemRequest) {
        if (!userRepository.findRoleById(principalId).equals(UserRole.STAFF)) {
            throw new InsufficientRoleException("You don't have enough role");
        }   

        UserItem userItem = userItemRepository.findById(createUpgraderItemRequest.getUserItemId())
                .orElse(null);

        UpgraderItem upgraderItem = UpgraderItem.builder()
                .itemId(userItem.getItemId())
                .build();

        upgraderItemRepository.save(upgraderItem);

        userItemRepository.delete(userItem);
    }
}
