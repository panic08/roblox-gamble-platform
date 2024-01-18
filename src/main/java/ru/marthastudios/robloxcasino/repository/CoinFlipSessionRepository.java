package ru.marthastudios.robloxcasino.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.marthastudios.robloxcasino.model.CoinFlipSession;

@Repository
public interface CoinFlipSessionRepository extends CrudRepository<CoinFlipSession, Long> {
}
