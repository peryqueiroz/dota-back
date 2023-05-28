package com.b1house.dotaslz.repository;

import com.b1house.dotaslz.dto.RankingPlayer;
import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository {

    Season findSeasonActivated();

    void saveScoreSeasonPlayer(Player player, Season season, Match match);

    List<RankingPlayer> getCurrentRankingOnActivatedSeason();

    void updateScoreOnSeason(Integer idMatch, Integer playerId, GameMode gameMode, Boolean isWin);
}
