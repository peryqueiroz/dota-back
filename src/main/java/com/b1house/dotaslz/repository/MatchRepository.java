package com.b1house.dotaslz.repository;

import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository {
    Match findMatchByIdDota(Player player, String idDota);

    Match findRecentMatchByPlayer(Integer playerId);

    Integer saveMatch(Match match);

    void updateGameMode(Integer matchId, GameMode gameMode);

    List<Match> findAll();
}
