package com.b1house.dotaslz.service.impl;

import com.b1house.dotaslz.dto.RankingPlayer;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
import com.b1house.dotaslz.repository.SeasonRepository;
import com.b1house.dotaslz.service.SeasonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    @Override
    public List<RankingPlayer> getCurrentRankingOnActivatedSeason() {
        List<RankingPlayer> rankingPlayers = seasonRepository.getCurrentRankingOnActivatedSeason();

        List<RankingPlayer> sortedList = new ArrayList<>(rankingPlayers);
        Collections.sort(sortedList, new Comparator<RankingPlayer>() {
            public int compare(RankingPlayer o1, RankingPlayer o2) {
                if (o1.getScore() == null && o2.getScore() == null) {
                    return 0;
                } else if (o1.getScore() == null) {
                    return 1;
                } else if (o2.getScore() == null) {
                    return -1;
                }

                int scoreComparison = o2.getScore().compareTo(o1.getScore());
                if (scoreComparison != 0) {
                    return scoreComparison;
                } else {
                    return Integer.compare(o2.getTotalMatches(), o1.getTotalMatches());
                }
            }
        });

        return sortedList;
    }
}
