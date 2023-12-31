package ru.marthastudios.robloxcasino.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.marthastudios.robloxcasino.dto.ErrorDto;
import ru.marthastudios.robloxcasino.dto.UserDto;
import ru.marthastudios.robloxcasino.exception.InvalidTokenException;
import ru.marthastudios.robloxcasino.payload.GetAllPhraseByRobloxNicknameResponse;
import ru.marthastudios.robloxcasino.service.implement.AuthServiceImpl;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "This component describes the authorization API")
public class AuthController {
    private final AuthServiceImpl authService;

    @Operation(description = "Authorize for further use of app-api")
    @PostMapping("/login")
    @ApiResponse(responseCode = "200", description = "We get the 'Authorization' token in the header, with which we can authorize in all api components")
    @ApiResponse(responseCode = "401", description = "Occurs when the server received an invalid 'Phrase-Token' in the header",
    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    @ApiResponse(responseCode = "404", description = "Occurs when the server did not find the user by roblox username from 'Phrase-Token'",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    @ApiResponse(responseCode = "409", description = "Occurs when the user description has no phrases from 'Phrase-Token'",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    public void handleLogin(HttpServletResponse httpServletResponse,
                                              @RequestHeader("Phrase-Token")
                                              @Parameter(description = "The 'Phrase-Token' we got from the header of the phrases we got from /api/v1/auth/phrase/getByRobloxNickname")
                                              String phraseToken){

        httpServletResponse.addHeader("Authorization",authService.handleLogin(phraseToken));
    }

    @Operation(description = "Get phrases for further authorization in /api/v1/auth/login")
    @ApiResponse(responseCode = "200", description = "Got the 'phrases' and header 'Phrase-Token'",
    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllPhraseByRobloxNicknameResponse.class))})
    @GetMapping("/phrase/getByRobloxNickname")
    public GetAllPhraseByRobloxNicknameResponse getAllPhraseByRobloxNickname(HttpServletResponse httpServletResponse,
                                                                             @RequestParam("roblox_nickname")
                                                                             @Parameter(description = "Nickname of roblox user, phrases for which we will get")
                                                                             String robloxNickname){
        GetAllPhraseByRobloxNicknameResponse getAllPhraseByRobloxNicknameResponse = authService.getAllPhraseByRobloxNickname(robloxNickname);

        httpServletResponse.addHeader("Phrase-Token", getAllPhraseByRobloxNicknameResponse.getPhraseToken());

        return getAllPhraseByRobloxNicknameResponse;
    }

    @GetMapping("/getUserInfo")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization",
            description = "We get the 'Authorization' token after authorization in the response of the /api/v1/auth/login method",
            required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "200", description = "The user has been successfully authorized and we have received 'User'",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))})
    @ApiResponse(responseCode = "403", description = "Occurs when a user has entered an invalid 'Authorization' token")
    public UserDto getUserInfo(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }
        return authService.getUserInfo((long) usernamePasswordAuthenticationToken.getPrincipal());
    }
}
