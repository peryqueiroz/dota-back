package com.b1house.dotaslz.service.impl;

import com.b1house.dotaslz.dto.MatchPlayers;
import com.b1house.dotaslz.dto.MatchQuantityPlayers;
import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
import com.b1house.dotaslz.repository.MatchRepository;
import com.b1house.dotaslz.service.MatchService;
import com.b1house.dotaslz.service.PlayerService;
import com.b1house.dotaslz.service.SeasonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {
    private MatchRepository matchRepository;
    private PlayerService playerService;

    private SeasonService seasonService;

    public MatchServiceImpl(MatchRepository matchRepository, PlayerService playerService, SeasonService seasonService){
        this.matchRepository = matchRepository;
        this.playerService = playerService;
        this.seasonService = seasonService;
    }
    @Override
    public Match getMatchByIdDota(Player player, String idDota) {
        return matchRepository.findMatchByIdDota(player, idDota);
    }

    @Override
    public List<Match> getRecentMatchesByAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        List<MatchPlayers> matches = new ArrayList<>();

        players.forEach(player ->{
            MatchPlayers matchPlayers = new MatchPlayers();
            matchPlayers.setPlayer(player);
            matchPlayers.setMatches(matchRepository.findRecentMatchByPlayer(player.getId()));
            matches.add(matchPlayers);
        });

        List unmodifiableList = Collections.unmodifiableList(matches);
        List newList = new ArrayList(unmodifiableList);
        Collections.sort(newList, new Comparator<MatchPlayers>() {
            public int compare(MatchPlayers o1, MatchPlayers o2) {
                if (o1.getMatches().get(0).getDate() == null || o2.getMatches().get(0).getDate()  == null)
                    return 0;
                return o2.getMatches().get(0).getDate().compareTo(o1.getMatches().get(0).getDate() );
            }
        });
        return newList;
    }

    @Override
    public Integer saveMatch(Match match) {
        return matchRepository.saveMatch(match);
    }

    @Override
    public void updateGameMode(Integer matchId, GameMode gameMode) {
        matchRepository.updateGameMode(matchId,gameMode);
    }

    @Override
    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    @Override
    public List<Match> getRecentMatches() {
        return matchRepository.findRecentMatches();
    }

    @Override
    public List<MatchQuantityPlayers> getMultiplePlayersByMatch() {
        Season season = new Season();
        try{
            season =seasonService.getSeasonActivated();
        } catch (Exception e){
            return   Collections.emptyList();
        }
        return matchRepository.findMultiplePlayersByMatch(season.getId());
    }

    @Override
    public void updateMatchInfos(Integer heroDamage, Integer towerDamage, Integer heroHealing, Integer imp, String award,
                                 Integer matchId) {
        matchRepository.updateMatchInfos(heroDamage,towerDamage,heroHealing,imp,award,matchId);
    }
}
