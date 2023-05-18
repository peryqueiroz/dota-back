package com.b1house.dotaslz.repository;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository {
    Match findMatchByIdDota(Player player, String idDota);

    Match findRecentMatchByPlayer(Integer playerId);

    Integer saveMatch(Match match);
}
