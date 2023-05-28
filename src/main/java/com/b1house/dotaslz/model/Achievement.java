package com.b1house.dotaslz.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("achievement")
public class Achievement {
    @Id
    private Integer id;
    private String type;
    private BigDecimal quantity;
    @MappedCollection(idColumn = "player_id", keyColumn = "id")
    private Player player;
    @Column("updated_at")
    private LocalDateTime updatedAt;

    public Achievement() {
    }

    public Achievement(Integer id, String type, BigDecimal quantity, Player player, LocalDateTime updatedAt) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.player = player;
        this.updatedAt = updatedAt;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
