package ru.marthastudios.robloxcasino.controller;

//import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.marthastudios.robloxcasino.dto.GameDto;
import ru.marthastudios.robloxcasino.dto.UserDto;
import ru.marthastudios.robloxcasino.dto.UserItemDto;
import ru.marthastudios.robloxcasino.payload.games.GetGameStatisticResponse;
import ru.marthastudios.robloxcasino.repository.ItemRepository;
import ru.marthastudios.robloxcasino.service.implement.UserServiceImpl;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "This component describes the Users API")
public class UserController {
    private final UserServiceImpl userService;
    @GetMapping("/getAuthInfo")
    @Operation(description = "Get information about the user by 'Authorization' token.")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization",
            description = "We get the 'Authorization' token after authorization in the response of the /api/v1/auth/login method",
            required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The user has been successfully authorized and we have received 'User'",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Occurs when a user has entered an invalid 'Authorization' token")
    })
    public UserDto getAuthInfo(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        return userService.getAuthInfo((long) usernamePasswordAuthenticationToken.getPrincipal());
    }

    @GetMapping("/game/getAuthStat")
    @Operation(description = "Get Auth user game stat by 'Authorization' token.")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization",
            description = "We get the 'Authorization' token after authorization in the response of the /api/v1/auth/login method",
            required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We got game stat of the Auth user",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetGameStatisticResponse.class))})
    })
    public GetGameStatisticResponse getAuthGameStat(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        return userService.getAuthGameStat((long) usernamePasswordAuthenticationToken.getPrincipal());
    }

    @GetMapping("/{id}/game/getAll")
    @Operation(description = "Get all user games by specified filter")
    public List<GameDto> getAllGame(@PathVariable("id") long id,
                                    @Parameter(description = "Minimum inclusive index from which user items will be returned")
                                    @RequestParam(value = "min_index", required = false) Integer minIndex,
                                    @Parameter(description = "The maximum index is not inclusive to which user items will be returned")
                                        @RequestParam(value = "max_index", required = false) Integer maxIndex){

        return userService.getAllGame(id, minIndex, maxIndex);
    }

    @GetMapping("/{id}/item/getAll")
    @Operation(description = "Get all user items by specified filter")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "We got all the 'UserItem' (items) of the user",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserItemDto.class)))})
    })
    public List<UserItemDto> getAllItem(@PathVariable("id") long id,
                                        @Parameter(description = "Minimum inclusive index from which user items will be returned")
                                        @RequestParam(value = "min_index", required = false) Integer minIndex,
                                        @Parameter(description = "The maximum index is not inclusive to which user items will be returned")
                                            @RequestParam(value = "max_index", required = false) Integer maxIndex){
        return userService.getAllItem(id, minIndex, maxIndex);
    }

}
