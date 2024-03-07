package ru.marthastudios.robloxcasino.scheduler;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
@Slf4j
public class ChatCooldown {
    private Map<Long, Long> principalIdCooldownMap = new HashMap<>();

    @Scheduled(fixedDelay = 36000000)
    public void handleCleanMap() {
        log.info("Starting cleaning principalIdCooldownMap in class {}", ChatCooldown.class);

        principalIdCooldownMap.clear();
    }
}
