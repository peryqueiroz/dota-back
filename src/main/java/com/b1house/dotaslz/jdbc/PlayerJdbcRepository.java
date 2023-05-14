package com.b1house.dotaslz.jdbc;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.PlayerRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class PlayerJdbcRepository implements PlayerRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PlayerJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final String FIND_ALL_PLAYER = """
        SELECT * FROM players 
        """;

    @Override
    public List<Player> findAllPlayers() {
        String query = FIND_ALL_PLAYER;

        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> {
            Player player = new Player();
            player.setId(rs.getInt("id"));
            player.setIdDota(rs.getString("id_dota"));
            player.setNome(rs.getString("nome"));
            player.setNick(rs.getString("nick"));
            return player;
        });
    }

    @Override
    public Player findPlayerById(Integer id) {
        String query = FIND_ALL_PLAYER;
        query += " WHERE id = :id ";
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(query, parameter,(rs, rowNum) -> {
            Player player = new Player();
            player.setId(rs.getInt("id"));
            player.setIdDota(rs.getString("id_dota"));
            player.setNome(rs.getString("nome"));
            player.setNick(rs.getString("nick"));

            return player;
        });
    }

    private BiFunction<ResultSet, Integer, Player> result() {
        return (rs, index) -> {
            try {
                return new Player(
                    rs.getInt("id"),
                    rs.getString("id_dota"),
                    rs.getString("nome"),
                    rs.getString("nick")

                );
            } catch (Exception e) {
                // Tratar o erro, se necessário
                return null;
            }
        };
    }
}
