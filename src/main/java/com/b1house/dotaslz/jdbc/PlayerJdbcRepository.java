package com.b1house.dotaslz.jdbc;

import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.PlayerRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
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
            player.setIdDota(rs.getInt("id_dota"));
            player.setNome(rs.getString("nome"));
            player.setNick(rs.getString("nick"));
            return player;
        });
    }

    @Override
    public Player findPlayerByIdDota() {
        return null;
    }

    private BiFunction<ResultSet, Integer, Player> result() {
        return (rs, index) -> {
            try {
                return new Player(
                    rs.getInt("id"),
                    rs.getInt("id_dota"),
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
