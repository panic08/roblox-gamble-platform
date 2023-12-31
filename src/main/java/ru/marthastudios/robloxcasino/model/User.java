package ru.marthastudios.robloxcasino.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import ru.marthastudios.robloxcasino.enums.UserRole;

@Table(name = "users_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Long id;
    private UserRole role;
    @MappedCollection(idColumn = "user_id")
    private UserRobloxData robloxData;
    @Column("registered_at")
    private Long registeredAt;
}
