package ru.marthastudios.robloxcasino.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(name = "coinflip_sessions_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinFlipSession {
    @Id
    private Long id;
    @Column("issuer_user_id")
    private Long issuerUserId;
    @Column("other_side_user_id")
    private Long otherSideUserId;
    @MappedCollection(idColumn = "session_id", keyColumn = "session_key")
    private List<CoinFlipSessionItem> items;
    @Column("server_seed")
    private String serverSeed;
    @Column("client_seed")
    private String clientSeed;
    private String salt;
    @Column("winner_user_id")
    private Long winnerUserId;
    @Column("created_at")
    private Long createdAt;
}
