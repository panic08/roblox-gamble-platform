package ru.marthastudios.robloxcasino.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.payload.CreateMessageRequest;
import ru.marthastudios.robloxcasino.service.implement.ChatServiceImpl;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "This component describes the Chat API")
public class ChatController {
    private final ChatServiceImpl chatService;

    @PostMapping
    @Operation(description = "Send message in chat")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization",
            description = "We get the 'Authorization' token after authorization in the response of the /api/v1/auth/login method",
            required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "A message can have a minimum of 1 character and a maximum of 100 characters" +
                    "| Message not may be empty",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "409", description = "The user does not have 5 games played",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    })
    public void createMessage(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,
                              @RequestBody @Valid @Parameter(description = "An object that contains 'message'") CreateMessageRequest createMessageRequest){
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        chatService.createMessage((long) usernamePasswordAuthenticationToken.getPrincipal(), createMessageRequest);
    }
}
