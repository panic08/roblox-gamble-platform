package ru.marthastudios.robloxcasino.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.marthastudios.robloxcasino.dto.CoinFlipSessionDto;
import ru.marthastudios.robloxcasino.dto.ItemDto;
import ru.marthastudios.robloxcasino.dto.UserDto;
import ru.marthastudios.robloxcasino.enums.GameType;
import ru.marthastudios.robloxcasino.exception.AccountsSamesException;
import ru.marthastudios.robloxcasino.exception.GameAlreadyStartedException;
import ru.marthastudios.robloxcasino.exception.IncorrectCostException;
import ru.marthastudios.robloxcasino.exception.ItemNotFoundException;
import ru.marthastudios.robloxcasino.mapper.CoinFlipSessionToCoinFlipSessionDtoMapperImpl;
import ru.marthastudios.robloxcasino.mapper.GameToGameDtoMapperImpl;
import ru.marthastudios.robloxcasino.mapper.ItemToItemDtoMapperImpl;
import ru.marthastudios.robloxcasino.mapper.UserToUserDtoMapperImpl;
import ru.marthastudios.robloxcasino.message.EventCoinFlipSessionMessage;
import ru.marthastudios.robloxcasino.model.*;
import ru.marthastudios.robloxcasino.payload.CreateCoinFlipSessionRequest;
import ru.marthastudios.robloxcasino.payload.CreateUpgraderRequest;
import ru.marthastudios.robloxcasino.payload.CreateUpgraderResponse;
import ru.marthastudios.robloxcasino.payload.JoinCoinFlipSessionRequest;
import ru.marthastudios.robloxcasino.repository.*;
import ru.marthastudios.robloxcasino.service.GameService;
import ru.marthastudios.robloxcasino.util.DiceRollUtil;

import java.util.ArrayList;
import java.util.List;

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
                .clientSeed(DiceRollUtil.generateSecret())
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

        System.out.println("issuerCost: " + issuerItemsCost + " otherSideCost:" + otherSideItemsCost
        + " percent lower: " + (issuerItemsCost - (issuerItemsCost * 10 / 100)) + " percent higher: " + (issuerItemsCost + (issuerItemsCost * 10 / 100)));

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
        return null;
    }
}
