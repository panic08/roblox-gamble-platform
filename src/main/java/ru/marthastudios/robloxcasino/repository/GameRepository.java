package ru.marthastudios.robloxcasino.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.marthastudios.robloxcasino.model.Game;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    @Query("SELECT g.* FROM games_table g WHERE g.user_id = :userId")
    List<Game> findAllByUserId(@Param("userId") long userId);

    @Query("SELECT g.* FROM games_table g WHERE g.user_id = :userId ORDER BY g.created_at DESC OFFSET :offset LIMIT :limit")
    List<Game> findAllByUserIdOrderByCreatedAtDescWithOffsetLimit(@Param("userId") long userId,
                                                     @Param("offset") int offset,
                                                     @Param("limit") int maxIndex);

    @Query("SELECT count(*) FROM games_table WHERE user_id = :userId")
    long countByUserId(@Param("userId") long userId);
}
