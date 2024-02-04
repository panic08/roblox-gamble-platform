package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.dto.GameDto;
import ru.marthastudios.robloxcasino.dto.UserDto;
import ru.marthastudios.robloxcasino.dto.UserItemDto;
import ru.marthastudios.robloxcasino.payload.games.GetGameStatisticResponse;

import java.util.List;

public interface UserService {
    UserDto getAuthInfo(long id);
    GetGameStatisticResponse getAuthGameStat(long principalId);
    List<UserItemDto> getAllItem(long id, Integer minIndex, Integer maxIndex);
    List<GameDto> getAllGame(long principalId, Integer minIndex, Integer maxIndex);
    UserDto getByRobloxId(long robloxId);
}
