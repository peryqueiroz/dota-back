package com.b1house.dotaslz.dto;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;

import java.util.List;

public class MatchPlayers {
    public MatchPlayers() {
    }

    private Player player;
    private List<Match> matches;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
