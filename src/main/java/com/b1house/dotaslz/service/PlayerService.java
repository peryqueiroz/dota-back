package com.b1house.dotaslz.service;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface PlayerService {

    List<Player> getAllPlayers();

    Player getPlayerById(Integer id);
    Player getPlayerByIdDota(String idDota);

    void updateAvatar(Player player);
    void updateNick(Player player);
    void updateIsPrivate(Player player);

    void updateStreak(Player player, Boolean win);
    Integer getStreakWin(Player player);
    Integer getStreakLoss(Player player);
}
