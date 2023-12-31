package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.payload.GetAllPhraseByRobloxNicknameResponse;

public interface AuthService {
    String handleLogin(String phraseToken);
    GetAllPhraseByRobloxNicknameResponse getAllPhraseByRobloxNickname(String robloxNickname);
}
