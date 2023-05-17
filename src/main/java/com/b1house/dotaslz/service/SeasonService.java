package com.b1house.dotaslz.service;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;

public interface SeasonService {

    Season getSeasonActivated();
    void saveScoreSeasonPlayer(Player player, Season season, Match match, Integer score);
}
