package ru.marthastudios.robloxcasino.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.marthastudios.robloxcasino.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u.id, u.role, u.registered_at FROM users_table u WHERE u.id = :id")
    User findByIdWithoutRobloxData(@Param("id") long id);

}
