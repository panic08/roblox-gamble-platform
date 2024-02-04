package ru.marthastudios.robloxcasino.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.marthastudios.robloxcasino.model.UserItem;

import java.util.List;

@Repository
public interface UserItemRepository extends CrudRepository<UserItem, Long> {
    @Query("SELECT ui.* FROM users_items_table ui WHERE ui.user_id = :userId OFFSET :offset LIMIT :limit")
    List<UserItem> findAllByUserIdWithOffsetAndLimit(@Param("userId") long userId,
                                                     @Param("offset") int offset,
                                                     @Param("limit") int limit);
    @Query("SELECT ui.* FROM users_items_table ui WHERE ui.user_id = :userId")
    List<UserItem> findAllByUserId(@Param("userId") long userId);
}
