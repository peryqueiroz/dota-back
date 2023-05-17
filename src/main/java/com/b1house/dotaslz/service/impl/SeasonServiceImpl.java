package com.b1house.dotaslz.service.impl;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
import com.b1house.dotaslz.repository.SeasonRepository;
import com.b1house.dotaslz.service.SeasonService;
import org.springframework.stereotype.Service;

@Service
public class SeasonServiceImpl implements SeasonService {
    private SeasonRepository seasonRepository;

    public SeasonServiceImpl(SeasonRepository seasonRepository){
        this.seasonRepository = seasonRepository;
    }
    @Override
    public Season getSeasonActivated() {
        return seasonRepository.findSeasonActivated();
    }

    @Override
    public void saveScoreSeasonPlayer(Player player, Season season, Match match, Integer score) {
        seasonRepository.saveScoreSeasonPlayer(player,season, match, score);
    }
}
