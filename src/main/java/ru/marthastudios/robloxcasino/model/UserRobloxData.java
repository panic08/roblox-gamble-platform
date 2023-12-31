package ru.marthastudios.robloxcasino.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users_roblox_data_table")
@Data
@Builder
public class UserRobloxData {
    @Id
    private Long id;
    @Column("roblox_id")
    private Long robloxId;
    @Column("roblox_nickname")
    private String robloxNickname;
    @Column("roblox_avatar_link")
    private String robloxAvatarLink;
}
