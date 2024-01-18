package ru.marthastudios.robloxcasino.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.marthastudios.robloxcasino.enums.GameType;

@Table(name = "games_table")
@Data
@Builder
public class Game {
    @Id
    private Long id;
    private GameType type;
    @Column("user_id")
    private Long userId;
    private Integer amount;
    @Column("is_win")
    private Boolean isWin;
    @Column("created_at")
    private Long createdAt;
}
