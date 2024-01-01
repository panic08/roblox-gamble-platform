package ru.marthastudios.robloxcasino.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users_items_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserItem {
    @Id
    private Long id;
    @Column("user_id")
    private Long userId;
    @Column("item_id")
    private Long itemId;
}
