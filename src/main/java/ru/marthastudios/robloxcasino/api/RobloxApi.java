package ru.marthastudios.robloxcasino.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.marthastudios.robloxcasino.api.payload.GetAvatarHeadshotResponse;
import ru.marthastudios.robloxcasino.api.payload.GetDetailedUserInformationResponse;
import ru.marthastudios.robloxcasino.api.payload.GetUsersByUsernameRequest;
import ru.marthastudios.robloxcasino.api.payload.GetUsersByUsernameResponse;

@Component
@RequiredArgsConstructor
public class RobloxApi {
    private final RestTemplate restTemplate;
    private static final String usersRobloxBaseURL = "https://users.roblox.com";
    private static final String thumbnailsRobloxBaseURL = "https://thumbnails.roblox.com";

    public GetUsersByUsernameResponse getUsersByUsername(GetUsersByUsernameRequest getUsersByUsernameRequest){
        ResponseEntity<GetUsersByUsernameResponse> getUsersByUsernameResponseResponseEntity =
                restTemplate.postForEntity(usersRobloxBaseURL + "/v1/usernames/users", getUsersByUsernameRequest, GetUsersByUsernameResponse.class);

        return getUsersByUsernameResponseResponseEntity.getBody();
    }

    public GetDetailedUserInformationResponse getDetailedUserInformationById(long id){
        ResponseEntity<GetDetailedUserInformationResponse> getDetailedUserInformationResponseResponseEntity =
                restTemplate.getForEntity(usersRobloxBaseURL + "/v1/users/" + id, GetDetailedUserInformationResponse.class);

        return getDetailedUserInformationResponseResponseEntity.getBody();
    }

    public GetAvatarHeadshotResponse getAvatarHeadshotByUserIdAndSizeAndFormatAndIsCircular(long userId,
                                                                                             String size,
                                                                                             String format,
                                                                                             boolean isCircular){
        ResponseEntity<GetAvatarHeadshotResponse> getAvatarHeadshotResponseResponseEntity =
                restTemplate.getForEntity(thumbnailsRobloxBaseURL + "/v1/users/avatar-headshot?userIds=" + userId
                + "&size=" + size
                + "&format=" + format
                + "&isCircular=" + isCircular, GetAvatarHeadshotResponse.class);

        return getAvatarHeadshotResponseResponseEntity.getBody();
    }
}
