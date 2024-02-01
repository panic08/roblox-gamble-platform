package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.payload.authorization.GetAllPhraseByRobloxNicknameResponse;

public interface AuthService {
    String handleLogin(String phraseToken);
    GetAllPhraseByRobloxNicknameResponse getAllPhraseByRobloxNickname(String robloxNickname);
}
