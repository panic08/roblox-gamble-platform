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
import ru.marthastudios.robloxcasino.payload.CreateWithdrawalRequest;
import ru.marthastudios.robloxcasino.service.implement.WithdrawalServiceImpl;

@RestController
@RequestMapping("/api/v1/withdrawal")
@RequiredArgsConstructor
@Tag(name = "Withdrawal", description = "This component describes the Withdrawal API")
public class WithdrawalController {
    private final WithdrawalServiceImpl withdrawalService;

    @PostMapping
    @Operation(description = "Create Withdrawal")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization",
            description = "We get the 'Authorization' token after authorization in the response of the /api/v1/auth/login method",
            required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Specify at least 1 user_item_id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "409", description = "The user does not have such an item",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
    })
    public void create(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,
                       @RequestBody @Valid @Parameter(description = "This object contains user_item_ds")
                       CreateWithdrawalRequest createWithdrawalRequest) {
        if (usernamePasswordAuthenticationToken == null){
            throw new AuthenticationCredentialsNotFoundException("Unauthorized");
        }

        withdrawalService.create((long) usernamePasswordAuthenticationToken.getPrincipal(), createWithdrawalRequest);
    }
}
