package ru.marthastudios.robloxcasino.service;

import ru.marthastudios.robloxcasino.dto.IncomingWithdrawalDto;
import ru.marthastudios.robloxcasino.payload.CreateWithdrawalRequest;

import java.util.List;

public interface WithdrawalService {
    void create(long principalId, CreateWithdrawalRequest createWithdrawalRequest);
    List<IncomingWithdrawalDto> getAllIncoming();
}
