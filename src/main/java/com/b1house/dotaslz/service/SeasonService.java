package com.b1house.dotaslz.service;

import com.b1house.dotaslz.dto.RankingPlayer;
import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;

import java.util.List;

public interface SeasonService {

    Season getSeasonActivated();
    void saveScoreSeasonPlayer(Player player, Season season, Match match);
    List<RankingPlayer> getCurrentRankingOnActivatedSeason();
    void updateGameModeOfMatchAndScoreOnSeason(String matchIdDota, List<String> playersIdDota, GameMode gameMode, Boolean isWin);
}
