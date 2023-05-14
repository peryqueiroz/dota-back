package com.b1house.dotaslz.jdbc;

import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.PlayerRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerJdbcRepository implements PlayerRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PlayerJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final String FIND_ALL_PLAYER = """
        SELECT * FROM players 
        """;

    final String UPDATE_AVATAR = """
        UPDATE players SET avatar = :avatar
        WHERE id = :id
        """;

    final String UPDATE_NICK = """
        UPDATE players SET nick = :nick
        WHERE id = :id
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
            player.setAvatar(rs.getString("avatar"));
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
            player.setAvatar(rs.getString("avatar"));

            return player;
        });
    }

    @Override
    public void updateAvatar(Player player) {
        String query = UPDATE_AVATAR;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("avatar", player.getAvatar());
        parameter.addValue("id", player.getId());

        namedParameterJdbcTemplate.update(query,parameter);

    }

    @Override
    public void updateNick(Player player) {
        String query = UPDATE_NICK;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("nick", player.getNick());
        parameter.addValue("id", player.getId());

        namedParameterJdbcTemplate.update(query,parameter);
    }
}
