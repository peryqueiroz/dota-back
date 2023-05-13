package com.b1house.dotaslz.service.impl;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.MatchRepository;
import com.b1house.dotaslz.service.MatchService;
import com.b1house.dotaslz.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {
    private MatchRepository matchRepository;
    private PlayerService playerService;

    public MatchServiceImpl(MatchRepository matchRepository, PlayerService playerService){
        this.matchRepository = matchRepository;
        this.playerService = playerService;
    }
    @Override
    public Match getMatchByIdDota(String idDota) {
        return matchRepository.findMatchByIdDota(idDota);
    }

    @Override
    public List<Match> getRecentMatchesByAllPlayers() {
        List<Player> players = playerService.getAllPlayers();

        return players.stream().map(player -> matchRepository.findRecentMatchByPlayer(player.getId())).toList();
    }
}
