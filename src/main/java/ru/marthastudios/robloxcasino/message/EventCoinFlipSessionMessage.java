package ru.marthastudios.robloxcasino.message;

import lombok.*;
import ru.marthastudios.robloxcasino.dto.CoinFlipSessionDto;

@Getter
@Setter
@Builder
public class EventCoinFlipSessionMessage {
    private String type;
    private CoinFlipSessionDto data;
}
