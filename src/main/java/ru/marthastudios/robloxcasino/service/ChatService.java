package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.payload.chat.CreateMessageRequest;

public interface ChatService {
    void createMessage(long principalId, CreateMessageRequest createMessageRequest);
    void createAdminMessage(long principalId, CreateMessageRequest createMessageRequest);
}
