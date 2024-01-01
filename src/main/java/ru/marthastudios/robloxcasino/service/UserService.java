package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.dto.UserDto;
import ru.marthastudios.robloxcasino.dto.UserItemDto;

import java.util.List;

public interface UserService {
    UserDto getAuthInfo(long principalId);
    List<UserItemDto> getAllItem(long id, Integer minIndex, Integer maxIndex);
}
