package ru.marthastudios.robloxcasino.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.marthastudios.robloxcasino.dto.IncomingWithdrawalDto;
import ru.marthastudios.robloxcasino.dto.UserItemDto;
import ru.marthastudios.robloxcasino.payload.user.CreateUserItemsRequest;
import ru.marthastudios.robloxcasino.service.implement.ApiServiceImpl;
import ru.marthastudios.robloxcasino.service.implement.WithdrawalServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Tag(name = "Public API", description = "This component describes the Public API")
public class ApiController {
    @Value("${publicApi.apiKey}")
    private String publicApiKey;
    private final ApiServiceImpl apiService;
    private final WithdrawalServiceImpl withdrawalService;

    @PostMapping("/user/{userId}/item/addAll")
    @Operation(description = "Add items to user by itemId of item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We added an items to a user by itemId and its userId"),
            @ApiResponse(responseCode = "401", description = "When our 'x-api-key' is wrong.")
    })
    public List<UserItemDto> createUserItems(@RequestHeader("X-API-KEY") String apiKey,
                                @PathVariable("userId") long userId,
                                @RequestBody CreateUserItemsRequest createUserItemRequest){
        if (!publicApiKey.equals(apiKey)){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        return apiService.createUserItems(userId, createUserItemRequest);
    }

    @DeleteMapping("/user/{userId}/item/{userItemId}")
    @Operation(description = "Remove item from user's inventory by userItemId of item in user's inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We removed an item from the user's inventory by the userItemId of the item in the user's inventory"),
            @ApiResponse(responseCode = "401", description = "When our 'x-api-key' is wrong.")
    })
    public void deleteUserItem(@RequestHeader("X-API-KEY") String apiKey,
                               @PathVariable("userId") long userId,
                               @Parameter(description = "userItemId of the item in the user's inventory") @PathVariable("userItemId") long userItemId){
        if (!publicApiKey.equals(apiKey)){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        apiService.deleteUserItem(userId, userItemId);
    }

    @GetMapping("/withdrawal/getAllIncoming")
    @Operation(description = "Get all incoming withdrawal")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "When our 'x-api-key' is wrong.")
    })
    public List<IncomingWithdrawalDto> getAllIncomingWithdrawal(@RequestHeader("X-API-KEY") String apiKey) {
        if (!publicApiKey.equals(apiKey)){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        return withdrawalService.getAllIncoming();
    }
}
