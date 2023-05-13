package com.b1house.dotaslz.jdbc;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.MatchRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class MatchJdbcRepository implements MatchRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MatchJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final String FIND_MATCH = """
        SELECT m.id AS match_id, m.id_dota AS match_id_dota, m.kills, m.deaths, m.assists,
        m.hero_id, m.date, p.id as player_id, p.id_dota as player_id_dota, p.nome, p.nick
        
        FROM matches m
        INNER JOIN players p on m.player_id = p.id 
        """;

    @Override
    public Match findMatchByIdDota(String idDota) {
        String query = FIND_MATCH;
        query += " WHERE m.id_dota = :id_dota";

        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("id_dota", idDota);

        return result(query, parameter);
    }

    @Override
    public Match findRecentMatchByPlayer(Integer playerId) {
        String query = FIND_MATCH;
        query += " where player_id = :player_id order by date desc limit 1";
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("player_id", playerId);

        return result(query, parameter);
    }

    private Match result(String query, MapSqlParameterSource parameter) {
        return namedParameterJdbcTemplate.queryForObject(query, parameter, (rs, rowNum) -> {
            Match match = new Match();
            match.setId(rs.getInt("match_id"));
            match.setIdDota(rs.getString("match_id_dota"));
            match.setKills(rs.getInt("kills"));
            match.setAssists(rs.getInt("assists"));
            match.setDeaths(rs.getInt("deaths"));
            match.setHeroId(rs.getInt("hero_id"));

            Timestamp timestamp = rs.getTimestamp("date");
            match.setDate(timestamp.toLocalDateTime());

            Player player = new Player();
            player.setId(rs.getInt("player_id"));
            player.setIdDota(rs.getString("player_id_dota"));
            player.setNome(rs.getString("nome"));
            player.setNick(rs.getString("nick"));
            match.setPlayer(player);

            return match;
        });
    }
}
