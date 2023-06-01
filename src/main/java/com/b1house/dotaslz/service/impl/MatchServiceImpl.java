package com.b1house.dotaslz.service.impl;

import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.MatchRepository;
import com.b1house.dotaslz.service.MatchService;
import com.b1house.dotaslz.service.PlayerService;
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
        List<Match> matches = players.stream().map(player -> matchRepository.findRecentMatchByPlayer(player.getId())).toList();

        List unmodifiableList = Collections.unmodifiableList(matches);
        List newList = new ArrayList(unmodifiableList);
        Collections.sort(newList, new Comparator<Match>() {
            public int compare(Match o1, Match o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o2.getDate().compareTo(o1.getDate());
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
}
