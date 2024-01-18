package ru.marthastudios.robloxcasino.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
public class GetAllPhraseByRobloxNicknameResponse {
    private String phrases;
    @JsonIgnore
    private String phraseToken;
}
