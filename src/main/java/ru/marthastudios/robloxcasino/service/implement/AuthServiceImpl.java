package ru.marthastudios.robloxcasino.service.implement;

import com.maximeroussy.invitrode.WordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.marthastudios.robloxcasino.api.RobloxApi;
import ru.marthastudios.robloxcasino.api.payload.GetUsersByUsernameRequest;
import ru.marthastudios.robloxcasino.api.payload.GetUsersByUsernameResponse;
import ru.marthastudios.robloxcasino.dto.UserDto;
import ru.marthastudios.robloxcasino.enums.UserRole;
import ru.marthastudios.robloxcasino.exception.AccountDescriptionNotMatchException;
import ru.marthastudios.robloxcasino.exception.AccountNonExistsException;
import ru.marthastudios.robloxcasino.exception.InvalidTokenException;
import ru.marthastudios.robloxcasino.mapper.UserToUserDtoMapperImpl;
import ru.marthastudios.robloxcasino.model.User;
import ru.marthastudios.robloxcasino.model.UserRobloxData;
import ru.marthastudios.robloxcasino.payload.GetAllPhraseByRobloxNicknameResponse;
import ru.marthastudios.robloxcasino.repository.UserRepository;
import ru.marthastudios.robloxcasino.repository.UserRobloxDataRepository;
import ru.marthastudios.robloxcasino.security.jwt.JwtAccessTokenUtil;
import ru.marthastudios.robloxcasino.security.jwt.JwtPhraseTokenUtil;
import ru.marthastudios.robloxcasino.service.AuthService;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RobloxApi robloxApi;
    private final JwtPhraseTokenUtil jwtPhraseTokenUtil;
    private final JwtAccessTokenUtil jwtAccessTokenUtil;
    private final UserRepository userRepository;
    private final UserRobloxDataRepository userRobloxDataRepository;
    private final UserToUserDtoMapperImpl userToUserDtoMapper;

    @Transactional
    @Override
    public String handleLogin(String phraseToken) {
        if (!jwtPhraseTokenUtil.validateToken(phraseToken)){
            throw new InvalidTokenException("Invalid phrase-token");
        }

        String robloxUsername = jwtPhraseTokenUtil.extractSubjectFromToken(phraseToken);
        String phrases = jwtPhraseTokenUtil.extractPhrasesFromToken(phraseToken);

        GetUsersByUsernameRequest getUsersByUsernameRequest = GetUsersByUsernameRequest.builder()
                .usernames(new String[] {robloxUsername})
                .isExcludeBannedUsers(true)
                .build();


        GetUsersByUsernameResponse getUsersByUsernameResponse = robloxApi.getUsersByUsername(getUsersByUsernameRequest);

        if (getUsersByUsernameResponse.getData().length == 0){
            throw new AccountNonExistsException("An account with that nickname does not exist.");
        }

        long robloxUserId = robloxApi.getUsersByUsername(getUsersByUsernameRequest).getData()[0].getId();

        String robloxUserDescription = robloxApi.getDetailedUserInformationById(robloxUserId).getDescription();

        if (!robloxUserDescription.equals(phrases)){
            throw new AccountDescriptionNotMatchException("The description doesn't match the phrases");
        }

        String robloxAvatarURL = robloxApi.getAvatarHeadshotByUserIdAndSizeAndFormatAndIsCircular(robloxUserId, "150x150", "Png", false)
                .getData()[0].getImageUrl();

        UserRobloxData userRobloxData = userRobloxDataRepository.findByRobloxId(robloxUserId);

        if (userRobloxData == null){
            UserRobloxData newUserRobloxData = UserRobloxData.builder()
                    .robloxId(robloxUserId)
                    .robloxNickname(robloxUsername)
                    .robloxAvatarLink(robloxAvatarURL)
                    .build();

            User newUser = User.builder()
                    .role(UserRole.DEFAULT)
                    .registeredAt(System.currentTimeMillis())
                    .robloxData(newUserRobloxData)
                    .build();

            newUser = userRepository.save(newUser);

            return jwtAccessTokenUtil.generateToken(newUser);
        } else {
            User user = userRepository.findByIdWithoutUserRobloxData(userRobloxDataRepository.findUserIdByRobloxId(robloxUserId));

            return jwtAccessTokenUtil.generateToken(user);
        }
    }

    @Override
    public GetAllPhraseByRobloxNicknameResponse getAllPhraseByRobloxNickname(String robloxNickname) {
        WordGenerator wordGenerator = new WordGenerator();
        StringBuilder phrases = new StringBuilder();

        for (int i = 0; i < 9; i++){
            if (i == 8){
                phrases.append(wordGenerator.newWord(4));
            } else {
                phrases.append(wordGenerator.newWord(4)).append(" ");
            }
        }


        return new GetAllPhraseByRobloxNicknameResponse(phrases.toString(), jwtPhraseTokenUtil.generateToken(robloxNickname, phrases.toString()));
    }

    @Override
    public UserDto getUserInfo(long principalId) {
        return userToUserDtoMapper.userToUserDto(userRepository.findById(principalId).orElse(null));
    }

}
