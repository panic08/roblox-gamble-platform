package ru.marthastudios.robloxcasino.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.marthastudios.robloxcasino.model.UpgraderItem;

import java.util.List;

@Repository
public interface UpgraderItemRepository extends CrudRepository<UpgraderItem, Long> {
    @Query("SELECT ui.* FROM upgrader_items_table ui OFFSET :minIndex LIMIT :maxIndex")
    List<UpgraderItem> findAllWithOffsetAndLimit(@Param("minIndex") int minIndex,
                                             @Param("maxIndex") int maxIndex);
}
