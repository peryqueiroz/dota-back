package com.b1house.dotaslz.repository;

import com.b1house.dotaslz.model.Player;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository {

    List<Player> findAllPlayers();
    Player findPlayerById(Integer id);
    Player findPlayerByIdDota(String idDota);
    void updateAvatar(Player player);
    void updateNick(Player player);
    void updateIsPrivate(Player player);
    void updateStreak(Player player, Boolean win);
    Integer getStreakWin(Player player);
    Integer getStreakLoss(Player player);
}
