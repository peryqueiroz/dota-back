package com.b1house.dotaslz.ExternalApi;

public class PlayerInfo {
    private int heroDamage;
    private int towerDamage;
    private int heroHealing;
    private int imp;
    private String award;
    private Boolean isParty;


    public int getHeroDamage() {
        return heroDamage;
    }

    public void setHeroDamage(int heroDamage) {
        this.heroDamage = heroDamage;
    }

    public int getTowerDamage() {
        return towerDamage;
    }

    public void setTowerDamage(int towerDamage) {
        this.towerDamage = towerDamage;
    }

    public int getHeroHealing() {
        return heroHealing;
    }

    public void setHeroHealing(int heroHealing) {
        this.heroHealing = heroHealing;
    }

    public int getImp() {
        return imp;
    }

    public void setImp(int imp) {
        this.imp = imp;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }
}
