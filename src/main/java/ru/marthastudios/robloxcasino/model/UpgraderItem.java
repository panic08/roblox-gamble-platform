package ru.marthastudios.robloxcasino.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "upgrader_items_table")
@Data
@Builder
public class UpgraderItem {
    @Id
    private Long id;
    @Column("item_id")
    private Long itemId;
}
