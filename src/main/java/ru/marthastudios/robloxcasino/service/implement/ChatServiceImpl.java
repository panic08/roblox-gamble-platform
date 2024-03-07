package ru.marthastudios.robloxcasino.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.marthastudios.robloxcasino.dto.UserDto;
import ru.marthastudios.robloxcasino.dto.UserRobloxDataDto;
import ru.marthastudios.robloxcasino.enums.UserRole;
import ru.marthastudios.robloxcasino.exception.ChatException;
import ru.marthastudios.robloxcasino.exception.InsufficientGamesException;
import ru.marthastudios.robloxcasino.exception.InsufficientRoleException;
import ru.marthastudios.robloxcasino.mapper.UserToUserDtoMapperImpl;
import ru.marthastudios.robloxcasino.message.EventChatMessageMessage;
import ru.marthastudios.robloxcasino.payload.chat.CreateMessageRequest;
import ru.marthastudios.robloxcasino.repository.GameRepository;
import ru.marthastudios.robloxcasino.repository.UserRepository;
import ru.marthastudios.robloxcasino.scheduler.ChatCooldown;
import ru.marthastudios.robloxcasino.service.ChatService;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final GameRepository gameRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;
    private final UserToUserDtoMapperImpl userToUserDtoMapper;
    private final ChatCooldown chatCooldown;

    @Value("${chat.filteredWords}")
    private String[] filteredWords;

    @Override
    public void createMessage(long principalId, CreateMessageRequest createMessageRequest) {
        long countPrincipalGames = gameRepository.countByUserId(principalId);

        if (countPrincipalGames < 5){
            throw new InsufficientGamesException("You don't have 5 games to write in the chat");
        }

        for (String filteredWord : filteredWords) {
            if (createMessageRequest.getMessage().contains(filteredWord)) {
                throw new ChatException("You sent a blocked word \"" + filteredWord + "\" in the chat");
            }
        }

        if (chatCooldown.getPrincipalIdCooldownMap().get(principalId) != null) {
            long timestamp = chatCooldown.getPrincipalIdCooldownMap().get(principalId);

            if (timestamp > System.currentTimeMillis()) {
                throw new ChatException("Wait " + (((timestamp - System.currentTimeMillis()) / 1000) + 1) + " sec before writing a new message");
            }
        }

        EventChatMessageMessage.Data eventChatMessageMessageData = EventChatMessageMessage.Data.builder()
                .user(userToUserDtoMapper.userToUserDto(userRepository.findById(principalId).orElse(null)))
                .message(createMessageRequest.getMessage())
                .build();

        EventChatMessageMessage eventChatMessageMessage = EventChatMessageMessage.builder()
                .type("create_chat_message")
                .data(eventChatMessageMessageData)
                .build();

        simpMessagingTemplate.convertAndSend("/chat/topic", eventChatMessageMessage);

        chatCooldown.getPrincipalIdCooldownMap().put(principalId, System.currentTimeMillis() + 4000);
    }

    @Override
    public void createAdminMessage(long principalId, CreateMessageRequest createMessageRequest) {
        if (!userRepository.findRoleById(principalId).equals(UserRole.STAFF)) {
            throw new InsufficientRoleException("You don't have enough role");
        }

        EventChatMessageMessage.Data eventChatMessageMessageData = EventChatMessageMessage.Data.builder()
                .user(UserDto.builder()
                        .id(0)
                        .role(UserRole.STAFF)
                        .robloxData(UserRobloxDataDto.builder()
                                .id(0)
                                .robloxId(0)
                                .robloxNickname("STAFF")
                                .robloxAvatarLink("https://tr.rbxcdn.com/c4265017c98559993061733b1125a23c/420/420/AvatarHeadshot/Png")
                                .build())
                        .registeredAt(System.currentTimeMillis())
                        .build())
                .message(createMessageRequest.getMessage())
                .build();

        EventChatMessageMessage eventChatMessageMessage = EventChatMessageMessage.builder()
                .type("create_chat_message")
                .data(eventChatMessageMessageData)
                .build();

        simpMessagingTemplate.convertAndSend("/chat/topic", eventChatMessageMessage);
    }
}
