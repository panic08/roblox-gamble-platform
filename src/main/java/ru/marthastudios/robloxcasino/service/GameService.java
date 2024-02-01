package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.dto.CoinFlipSessionDto;
import ru.marthastudios.robloxcasino.dto.UpgraderItemDto;
import ru.marthastudios.robloxcasino.payload.games.*;

import java.util.List;

public interface GameService {
    void createCoinFlipSession(long principalId, CreateCoinFlipSessionRequest createCoinFlipSessionRequest);
    List<CoinFlipSessionDto> getAllCoinFlipSession();
    void joinCoinFlipSession(long principalId, long id, JoinCoinFlipSessionRequest joinCoinFlipSessionRequest);
    CreateUpgraderResponse createUpgrader(long principalId, CreateUpgraderRequest createUpgraderRequest);
    List<UpgraderItemDto> getAllUpgraderItem(Integer minIndex, Integer maxIndex);
    void createUpgraderItem(long principalId, CreateUpgraderItemRequest createUpgraderItemRequest);
}
