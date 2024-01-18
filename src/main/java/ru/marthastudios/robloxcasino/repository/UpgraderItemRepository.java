package ru.marthastudios.robloxcasino.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.marthastudios.robloxcasino.model.UpgraderItem;

@Repository
public interface UpgraderItemRepository extends CrudRepository<UpgraderItem, Long> {
}
