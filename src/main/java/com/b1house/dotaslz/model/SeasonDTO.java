package com.b1house.dotaslz.model;

public class SeasonDTO {

    private Player player;
    private Season season;
    private Integer score;

    public SeasonDTO(){}
    public SeasonDTO(Player player, Season season, Integer score) {
        this.player = player;
        this.season = season;
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
