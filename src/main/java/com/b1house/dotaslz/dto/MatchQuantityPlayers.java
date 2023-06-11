package com.b1house.dotaslz.dto;

public class MatchQuantityPlayers {

    private String idDota;
    private Integer quantity;
    private Boolean win;

    public MatchQuantityPlayers() {
    }

    public String getIdDota() {
        return idDota;
    }

    public void setIdDota(String idDota) {
        this.idDota = idDota;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getWin() {
        return win;
    }

    public void setWin(Boolean win) {
        this.win = win;
    }
}
