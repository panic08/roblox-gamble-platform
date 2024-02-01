package ru.marthastudios.robloxcasino.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.marthastudios.robloxcasino.model.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
    @Query("SELECT i.cost FROM items_table i WHERE i.id = :id")
    int findCostById(@Param("id") long id);

}
