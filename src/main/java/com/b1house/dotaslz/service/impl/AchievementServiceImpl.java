package com.b1house.dotaslz.service.impl;

import com.b1house.dotaslz.model.Achievement;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.AchievementRepository;
import com.b1house.dotaslz.service.AchievementService;
import com.b1house.dotaslz.service.PlayerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

    private AchievementRepository achievementRepository;
    private PlayerService playerService;

    public AchievementServiceImpl(AchievementRepository achievementRepository, PlayerService playerService){
        this.achievementRepository = achievementRepository;
        this.playerService = playerService;
    }

    @Override
    public void updateWinLoss(Player player, Match match) {
        playerService.updateStreak(player, match.getWin());
        if(match.getWin()){
            Integer streakPlayer = playerService.getStreakWin(player);
            Integer streakCurrent = achievementRepository.getQuantitiyByType("WIN").intValueExact();

            if(streakPlayer > streakCurrent){
                Achievement achievement = new Achievement();
                achievement.setPlayer(player);
                achievement.setQuantity(BigDecimal.valueOf(streakPlayer));
                achievement.setType("WIN");
                achievement.setUpdatedAt(LocalDateTime.now());
                achievementRepository.edit(achievement);
            }
        }
        else{
            Integer streakPlayer = playerService.getStreakLoss(player);
            Integer streakCurrent = achievementRepository.getQuantitiyByType("LOSS").intValueExact();

            if(streakPlayer > streakCurrent){
                Achievement achievement = new Achievement();
                achievement.setPlayer(player);
                achievement.setQuantity(BigDecimal.valueOf(streakPlayer));
                achievement.setType("LOSS");
                achievement.setUpdatedAt(LocalDateTime.now());
                achievementRepository.edit(achievement);
            }
        }
    }

    @Override
    public void updateKda(Player player, Match match) {
        BigDecimal currentKdaPositive = achievementRepository.getQuantitiyByType("KDA_POSITIVE");
        BigDecimal currentKdaNegative = achievementRepository.getQuantitiyByType("KDA_NEGATIVE");

        Integer deaths = match.getDeaths() == 0 ? 1 : match.getDeaths();

        Integer killAssist = match.getKills() + match.getAssists();
        BigDecimal playerKda = BigDecimal.valueOf(Math.floorDiv(killAssist, deaths));

        if(playerKda.compareTo(currentKdaPositive) > 0){
            Achievement achievement = new Achievement();
            achievement.setPlayer(player);
            achievement.setQuantity(playerKda);
            achievement.setType("KDA_POSITIVE");
            achievement.setUpdatedAt(LocalDateTime.now());
            achievementRepository.edit(achievement);
        }
        if(playerKda.compareTo(currentKdaNegative) < 0){
            Achievement achievement = new Achievement();
            achievement.setPlayer(player);
            achievement.setQuantity(playerKda);
            achievement.setType("KDA_NEGATIVE");
            achievement.setUpdatedAt(LocalDateTime.now());
            achievementRepository.edit(achievement);
        }
    }

    @Override
    public BigDecimal getQuantityByType(String type) {
        return achievementRepository.getQuantitiyByType(type);
    }

    @Override
    public List<Achievement> findAll() {
        return achievementRepository.findAll();
    }
}
