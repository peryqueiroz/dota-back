package com.b1house.dotaslz.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("matches")
public class Match {
    @Id
    private Integer id;
    @Column("id_dota")
    private String idDota;
    @MappedCollection(idColumn = "player_id", keyColumn = "id")
    private Player player;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Integer heroId;
    @Column("date")
    private LocalDateTime date;

    public Match(Integer id, String idDota, Player player, Integer kills, Integer deaths, Integer assists, Integer heroId, LocalDateTime date) {
        this.id = id;
        this.idDota = idDota;
        this.player = player;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.heroId = heroId;
        this.date = date;
    }

    public Match() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdDota() {
        return idDota;
    }

    public void setIdDota(String idDota) {
        this.idDota = idDota;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getHeroId() {
        return heroId;
    }

    public void setHeroId(Integer heroId) {
        this.heroId = heroId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
