package com.b1house.dotaslz.service;

import com.b1house.dotaslz.model.Achievement;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;

import java.math.BigDecimal;
import java.util.List;

public interface AchievementService {

    void updateWinLoss(Player player, Match match);

    void updateKda(Player player, Match match);

    BigDecimal getQuantityByType(String type);

    List<Achievement> findAll();
}
