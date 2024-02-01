package ru.marthastudios.robloxcasino.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.marthastudios.robloxcasino.dto.*;
import ru.marthastudios.robloxcasino.payload.games.*;
import ru.marthastudios.robloxcasino.service.implement.GameServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
@Tag(name = "Games", description = "This component describes the Games API")
public class GameController {
    private final GameServiceImpl gameService;

    @PostMapping("/coinflip")
    @Operation(description = "Creating a Coinflip Game Session")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization",
            description = "We get the 'Authorization' token after authorization in the response of the /api/v1/auth/login method",
            required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "The user does not have such an item",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Specify at least 1 user_item_id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    })
    public void createCoinFlipSession(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,
                       @RequestBody @Valid @Parameter(description = "An object that contains an array of the user_item_id")
                       CreateCoinFlipSessionRequest createCoinFlipSessionRequest){
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        gameService.createCoinFlipSession((long) usernamePasswordAuthenticationToken.getPrincipal(), createCoinFlipSessionRequest);
    }

    @PostMapping("/coinflip/{id}/join")
    @Operation(description = "Join to current CoinFlip Game session")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization",
            description = "We get the 'Authorization' token after authorization in the response of the /api/v1/auth/login method",
            required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Specify at least 1 user_item_id | Specify the correct session_id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "409", description = "The user does not have such an item | You cannot log in to the CoinFlipSession you created | You cannot log in to an already completed CoinFlipSession" +
                    "| The sum of items did not fall within the range of the value of the sum of items issuer value",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
    })
    public void joinCoinFlipSession(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,
                                    @PathVariable("id") @Parameter(description = "CoinFlipSession id") long id,
                                    @RequestBody @Valid @Parameter(description = "An object that contains an array of the user_item_id and coinFlip session_id")
                                    JoinCoinFlipSessionRequest joinCoinFlipSessionRequest){
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }
;
        gameService.joinCoinFlipSession((long) usernamePasswordAuthenticationToken.getPrincipal(),
                id, joinCoinFlipSessionRequest);
    }

    @GetMapping("/coinflip/getAll")
    @Operation(description = "Get all CoinFlip Game session")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We got all the 'CoinFlipSession'",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CoinFlipSessionDto.class)))})
    })
    public List<CoinFlipSessionDto> getAllCoinFlipSession(){
        return gameService.getAllCoinFlipSession();
    }

    @PostMapping("/upgrader")
    @Operation(description = "Creating a Upgrader game")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization",
            description = "We get the 'Authorization' token after authorization in the response of the /api/v1/auth/login method",
            required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We got a CreateUpgraderResponse object that contains \"win\", from which we will determine whether the user has won or not"),
            @ApiResponse(responseCode = "409", description = "FromUserItem cannot be priced greater than or equal to ToUserItem",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    })
    public CreateUpgraderResponse createUpgrader(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,
                                                 @RequestBody @Parameter(description = "An object that contains from_user_item_id and to_upgrader_item_id")
                                                 CreateUpgraderRequest createUpgraderRequest) {
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        return gameService.createUpgrader((long) usernamePasswordAuthenticationToken.getPrincipal(), createUpgraderRequest);
    }

    @GetMapping("/upgrader/item/getAll")
    @Operation(description = "Get all UpgraderItem")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We got all UpgraderItem")
    })
    public List<UpgraderItemDto> getAllUpgraderItem(@Parameter(description = "Minimum inclusive index from which upgrader items will be returned")
                                                        @RequestParam(value = "min_index", required = false) Integer minIndex,
                                                    @Parameter(description = "The maximum index is not inclusive to which upgrader items will be returned")
                                                        @RequestParam(value = "max_index", required = false) Integer maxIndex) {
        return gameService.getAllUpgraderItem(minIndex, maxIndex);
    }

    @PostMapping("/upgrader/item")
    @Operation(description = "Creating a UpgraderItem from UserItem")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization",
            description = "We get the 'Authorization' token after authorization in the response of the /api/v1/auth/login method",
            required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We create a UpgraderItem from UserItem"),
            @ApiResponse(responseCode = "409", description = "You don't have enough role",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    })
    public void createUpgraderItem(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,
                                   @Parameter(description = "An object that contains user_item_id for create UpgraderItem")
                                    @RequestBody CreateUpgraderItemRequest createUpgraderItemRequest) {
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        gameService.createUpgraderItem((long) usernamePasswordAuthenticationToken.getPrincipal(), createUpgraderItemRequest);
    }

}
