package com.b1house.dotaslz.service;


import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;

import java.util.List;

public interface MatchService {
    Match getMatchByIdDota(Player player, String idDota);

    List<Match> getRecentMatchesByAllPlayers();

    Integer saveMatch(Match match);
}
