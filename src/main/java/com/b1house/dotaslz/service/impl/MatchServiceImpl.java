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

    public MatchServiceImpl(MatchRepository matchRepository, PlayerService playerService){
        this.matchRepository = matchRepository;
        this.playerService = playerService;
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
            try{
                matchPlayers.setMatches(matchRepository.findRecentMatchByPlayer(player.getId()));
                matches.add(matchPlayers);
            } catch (Exception e){
                System.out.println("Exception no findRecentMatch :"+ e.getMessage());
            }

        });
        List unmodifiableList = Collections.unmodifiableList(matches);
        List newList = new ArrayList(unmodifiableList);
        try{
            Collections.sort(newList, new Comparator<MatchPlayers>() {
                public int compare(MatchPlayers o1, MatchPlayers o2) {
                    if (o1.getMatches().get(0).getDate() == null || o2.getMatches().get(0).getDate()  == null)
                        return 0;
                    return o2.getMatches().get(0).getDate().compareTo(o1.getMatches().get(0).getDate() );
                }
            });
        } catch (Exception e){
            System.out.println("Error na comparação "+ e.getMessage());
        }

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
    public List<MatchQuantityPlayers> getMultiplePlayersByMatch(Integer seasonId) {
        return matchRepository.findMultiplePlayersByMatch(seasonId);
    }

    @Override
    public void updateMatchInfos(Integer heroDamage, Integer towerDamage, Integer heroHealing, Integer imp, String award,
                                 Integer matchId) {
        matchRepository.updateMatchInfos(heroDamage,towerDamage,heroHealing,imp,award,matchId);
    }
}
