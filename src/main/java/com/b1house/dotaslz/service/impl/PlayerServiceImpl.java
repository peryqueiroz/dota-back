package com.b1house.dotaslz.service.impl;

import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.PlayerRepository;
import com.b1house.dotaslz.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }
    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAllPlayers();
    }

    @Override
    public Player getPlayerById(Integer id) {
        return playerRepository.findPlayerById(id);
    }
}
