package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.dto.CoinFlipSessionDto;
import ru.marthastudios.robloxcasino.payload.CreateCoinFlipSessionRequest;
import ru.marthastudios.robloxcasino.payload.CreateUpgraderRequest;
import ru.marthastudios.robloxcasino.payload.CreateUpgraderResponse;
import ru.marthastudios.robloxcasino.payload.JoinCoinFlipSessionRequest;

import java.util.List;

public interface GameService {
    void createCoinFlipSession(long principalId, CreateCoinFlipSessionRequest createCoinFlipSessionRequest);
    List<CoinFlipSessionDto> getAllCoinFlipSession();
    void joinCoinFlipSession(long principalId, long id, JoinCoinFlipSessionRequest joinCoinFlipSessionRequest);
    CreateUpgraderResponse createUpgrader(long principalId, CreateUpgraderRequest createUpgraderRequest);
}
