package com.b1house.dotaslz.repository;

import com.b1house.dotaslz.model.Match;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository {
    Match findMatchByIdDota(String idDota);

    Match findRecentMatchByPlayer(Integer playerId);
}
