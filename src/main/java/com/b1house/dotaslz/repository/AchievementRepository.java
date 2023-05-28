package com.b1house.dotaslz.repository;

import com.b1house.dotaslz.model.Achievement;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AchievementRepository {

    void edit(Achievement achievement);

    BigDecimal getQuantitiyByType(String type);

    List<Achievement> findAll();
}
