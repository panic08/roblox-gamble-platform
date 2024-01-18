package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.payload.CreateMessageRequest;

public interface ChatService {
    void createMessage(long principalId, CreateMessageRequest createMessageRequest);
}
