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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.marthastudios.robloxcasino.dto.CoinFlipSessionDto;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.dto.GameDto;
import ru.marthastudios.robloxcasino.dto.UserItemDto;
import ru.marthastudios.robloxcasino.payload.CreateCoinFlipSessionRequest;
import ru.marthastudios.robloxcasino.payload.CreateUpgraderRequest;
import ru.marthastudios.robloxcasino.payload.CreateUpgraderResponse;
import ru.marthastudios.robloxcasino.payload.JoinCoinFlipSessionRequest;
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
            @ApiResponse(responseCode = "409", description = "The user does not have such an item",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    })
    public CreateUpgraderResponse createUpgrader(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,
                                                 @RequestBody @Valid @Parameter(description = "An object that contains user_item_id and upgrader_item_id")
                                                 CreateUpgraderRequest createUpgraderRequest) {
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        return gameService.createUpgrader((long) usernamePasswordAuthenticationToken.getPrincipal(), createUpgraderRequest);
    }
}
