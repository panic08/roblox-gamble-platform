package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.dto.UserItemDto;
import ru.marthastudios.robloxcasino.payload.user.CreateUserItemsRequest;

import java.util.List;

public interface ApiService {
    List<UserItemDto> createUserItems(long userId, CreateUserItemsRequest createUserItemsRequest);
    void deleteUserItem(long userId, long userItemId);
}
