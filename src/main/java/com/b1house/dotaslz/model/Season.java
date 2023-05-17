package com.b1house.dotaslz.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("season")
public class Season {
    @Id
    private Integer id;
    private String version;
    @MappedCollection(idColumn = "winner_id", keyColumn = "id")
    private Player player;
    private Integer score;
    @Column("date_start")
    private LocalDateTime dateStart;
    @Column("date_end")
    private LocalDateTime dateEnd;
    private Boolean actived;
    @MappedCollection(idColumn = "match_id", keyColumn = "id")
    private Match match;

    public Season() {
    }

    public Season(Integer id, String version, Player player, Integer score,
                  LocalDateTime dateStart, LocalDateTime dateEnd, Boolean actived, Match match) {
        this.id = id;
        this.version = version;
        this.player = player;
        this.score = score;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.actived = actived;
        this.match = match;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Boolean getActived() {
        return actived;
    }

    public void setActived(Boolean actived) {
        this.actived = actived;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
