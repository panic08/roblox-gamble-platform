package ru.marthastudios.robloxcasino.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllPhraseByRobloxNicknameResponse {
    private String phrases;
    @JsonIgnore
    private String phraseToken;
}
