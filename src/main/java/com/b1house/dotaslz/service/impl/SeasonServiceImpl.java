package com.b1house.dotaslz.service.impl;

import com.b1house.dotaslz.dto.RankingPlayer;
import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
import com.b1house.dotaslz.repository.SeasonRepository;
import com.b1house.dotaslz.service.MatchService;
import com.b1house.dotaslz.service.PlayerService;
import com.b1house.dotaslz.service.SeasonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class SeasonServiceImpl implements SeasonService {
    private SeasonRepository seasonRepository;
    private MatchService matchService;
    private PlayerService playerService;

    public SeasonServiceImpl(SeasonRepository seasonRepository, MatchService matchService, PlayerService playerService){
        this.seasonRepository = seasonRepository;
        this.matchService = matchService;
        this.playerService = playerService;
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

    @Override
    public void updateGameModeOfMatchAndScoreOnSeason(String matchIdDota, List<String> playersIdDota, GameMode gameMode, Boolean isWin) {
        playersIdDota.forEach(playerIdDota->{
            Player player = getPlayerByIdDota(playerIdDota);
            Match match = getMatchByIdDota(player, matchIdDota);
            updateGameModeMatch(match.getId(), gameMode);
            seasonRepository.updateScoreOnSeason(match.getId(),player.getId(),gameMode,isWin);
        });
    }

    private Player getPlayerByIdDota(String idDota){
        return playerService.getPlayerByIdDota(idDota);
    }
    private Match getMatchByIdDota(Player player, String matchIdDota){
        return matchService.getMatchByIdDota(player,matchIdDota);
    }
    private void updateGameModeMatch(Integer matchId, GameMode gameMode){
        matchService.updateGameMode(matchId, gameMode);
    }
}
