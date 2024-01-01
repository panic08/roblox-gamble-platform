package ru.marthastudios.robloxcasino.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.marthastudios.robloxcasino.model.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

}
