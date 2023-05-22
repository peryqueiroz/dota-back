package com.b1house.dotaslz.repository;

import com.b1house.dotaslz.dto.RankingPlayer;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;

import java.util.List;

public interface SeasonRepository {

    Season findSeasonActivated();

    void saveScoreSeasonPlayer(Player player, Season season, Match match, Integer score);

    List<RankingPlayer> getCurrentRankingOnActivatedSeason();
}
