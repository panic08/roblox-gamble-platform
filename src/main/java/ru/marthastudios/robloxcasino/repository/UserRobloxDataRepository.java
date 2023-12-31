package ru.marthastudios.robloxcasino.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.marthastudios.robloxcasino.model.UserRobloxData;

@Repository
public interface UserRobloxDataRepository extends CrudRepository<UserRobloxData, Long> {
    @Query("SELECT urd.* FROM users_roblox_data_table urd WHERE urd.roblox_id = :robloxId")
    UserRobloxData findByRobloxId(@Param("robloxId") long robloxId);

    @Query("SELECT urd.user_id FROM users_roblox_data_table urd WHERE urd.roblox_id = :robloxId")
    long findUserIdByRobloxId(@Param("robloxId") long robloxId);
}
