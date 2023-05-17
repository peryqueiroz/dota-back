package com.b1house.dotaslz.jdbc;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
import com.b1house.dotaslz.repository.SeasonRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class SeasonJdbcRepository implements SeasonRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SeasonJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final String FIND_SEASON = """
        SELECT * FROM season         
        """;

    final String INSERT_SCORE = """
        INSERT INTO season_players (player_id, season_id, score, match_id) VALUES (:player_id, :season_id, :score, :match_id)        
        """;

    @Override
    public Season findSeasonActivated() {
        String query = FIND_SEASON;
        query += " WHERE actived = true AND timezone('GMT+3', now()) BETWEEN season.date_start AND season.date_end; ";
        try{
            return result(query,null);
        } catch (IncorrectResultSizeDataAccessException e){
            System.out.println("More than one season activated returned on query - " + new Timestamp(System.currentTimeMillis()));
            throw new IncorrectResultSizeDataAccessException(1);
        }

    }

    @Override
    public void saveScoreSeasonPlayer(Player player, Season season, Match match, Integer score) {
        String query = INSERT_SCORE;
        MapSqlParameterSource parameter = new MapSqlParameterSource();

        parameter.addValue("player_id", player.getId());
        parameter.addValue("season_id", season.getId());
        parameter.addValue("score", score);
        parameter.addValue("match_id", match.getId());

        namedParameterJdbcTemplate.update(query,parameter);
    }

    private Season result(String query, MapSqlParameterSource parameter) {
        return namedParameterJdbcTemplate.queryForObject(query, parameter, (rs, rowNum) -> {
            Season season = new Season();
            season.setId(rs.getInt("id"));
            season.setVersion(rs.getString("version"));
            season.setActived(rs.getBoolean("actived"));

            Timestamp timestampStart = rs.getTimestamp("date_start");
            Timestamp timestampEnd = rs.getTimestamp("date_end");
            season.setDateStart(timestampStart.toLocalDateTime());
            season.setDateEnd(timestampEnd.toLocalDateTime());
            return season;
        });
    }
}