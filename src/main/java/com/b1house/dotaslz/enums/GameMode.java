package com.b1house.dotaslz.enums;

public enum GameMode {
    PARTY(20),
    SOLO(30);

    private int score;

    GameMode(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
