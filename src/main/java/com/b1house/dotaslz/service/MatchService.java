package com.b1house.dotaslz.service;


import com.b1house.dotaslz.model.Match;

import java.util.List;

public interface MatchService {
    Match getMatchByIdDota(String idDota);

    List<Match> getRecentMatchesByAllPlayers();
}
