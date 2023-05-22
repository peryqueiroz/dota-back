package com.b1house.dotaslz.dto;

public class RankingPlayer {
    private String name;
    private String nick;
    private Integer score;
    private Integer winParty;
    private Integer lossParty;
    private Integer winSolo;
    private Integer lossSolo;
    private Integer totalMatches;
    private Boolean isPlayerEligible;

    public RankingPlayer(String name, String nick, Integer score, Integer winParty, Integer lossParty, Integer winSolo, Integer lossSolo, Integer totalMatches, Boolean isPlayerEligible) {
        this.name = name;
        this.nick = nick;
        this.score = score;
        this.winParty = winParty;
        this.lossParty = lossParty;
        this.winSolo = winSolo;
        this.lossSolo = lossSolo;
        this.totalMatches = totalMatches;
        this.isPlayerEligible = isPlayerEligible;
    }

    public RankingPlayer() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getScore() {
        return this.score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getWinParty() {
        return this.winParty;
    }

    public void setWinParty(Integer winParty) {
        this.winParty = winParty;
    }

    public Integer getLossParty() {
        return this.lossParty;
    }

    public void setLossParty(Integer lossParty) {
        this.lossParty = lossParty;
    }

    public Integer getWinSolo() {
        return this.winSolo;
    }

    public void setWinSolo(Integer winSolo) {
        this.winSolo = winSolo;
    }

    public Integer getLossSolo() {
        return lossSolo;
    }

    public void setLossSolo(Integer lossSolo) {
        this.lossSolo = lossSolo;
    }

    public Integer getTotalMatches() {
        return this.totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public Boolean getIsPlayerEligible() {
        return this.isPlayerEligible;
    }

    public void setIsPlayerEligible(Boolean playerEligible) {
        this.isPlayerEligible = playerEligible;
    }
}
