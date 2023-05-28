package com.b1house.dotaslz.jdbc;

import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.PlayerRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    final String UPDATE_PRIVATE = """
        UPDATE players SET is_private = :is_private
        WHERE id = :id
        """;

    final String COUNT_STREAK_WIN = """
        UPDATE players SET streak_win = streak_win + 1
        WHERE id = :id;
        """;
    final String COUNT_STREAK_LOSS = """
        UPDATE players SET streak_loss = streak_loss + 1
        WHERE id = :id;
        """;

    final String RESET_STREAK_WIN = """
        UPDATE players SET streak_win = 0
        where id = :id
        """;

    final String RESET_STREAK_LOSS = """
        UPDATE players SET streak_loss = 0
        where id = :id
        """;

    final String GET_STREAK_WIN = """
        SELECT streak_win from players where id = :id
        """;

    final String GET_STREAK_LOSS = """
        SELECT streak_loss from players where id = :id
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
            player.setIsPrivate(rs.getBoolean("is_private"));
            player.setMmr(rs.getInt("mmr"));
            player.setIsMain(rs.getBoolean("is_main"));

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
            player.setIsPrivate(rs.getBoolean("is_private"));
            player.setMmr(rs.getInt("mmr"));
            player.setIsMain(rs.getBoolean("is_main"));

            return player;
        });
    }

    @Override
    public Player findPlayerByIdDota(String idDota) {
        String query = FIND_ALL_PLAYER;
        query += " WHERE id_dota = :id_dota ";
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("id_dota", idDota);

        return namedParameterJdbcTemplate.queryForObject(query, parameter,(rs, rowNum) -> {
            Player player = new Player();
            player.setId(rs.getInt("id"));
            player.setIdDota(rs.getString("id_dota"));
            player.setNome(rs.getString("nome"));
            player.setNick(rs.getString("nick"));
            player.setAvatar(rs.getString("avatar"));
            player.setIsPrivate(rs.getBoolean("is_private"));
            player.setMmr(rs.getInt("mmr"));
            player.setIsMain(rs.getBoolean("is_main"));

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

    @Override
    public void updateIsPrivate(Player player) {
        String query = UPDATE_PRIVATE;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("is_private", player.getIsPrivate());
        parameter.addValue("id", player.getId());

        namedParameterJdbcTemplate.update(query,parameter);
    }

    @Override
    public void updateStreak(Player player,Boolean win) {
        String query = win? COUNT_STREAK_WIN : COUNT_STREAK_LOSS ;
        String queryReset = win? RESET_STREAK_LOSS : RESET_STREAK_WIN;

        MapSqlParameterSource parameter = new MapSqlParameterSource();
        MapSqlParameterSource parameterReset = new MapSqlParameterSource();

        parameter.addValue("id", player.getId());
        namedParameterJdbcTemplate.update(query,parameter);

        parameterReset.addValue("id", player.getId());
        namedParameterJdbcTemplate.update(queryReset, parameterReset);
    }

    @Override
    public Integer getStreakWin(Player player) {
        String query = GET_STREAK_WIN;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("id", player.getId());

        return namedParameterJdbcTemplate.queryForObject(query,parameter, BigDecimal.class).intValueExact();
    }

    @Override
    public Integer getStreakLoss(Player player) {
        String query = GET_STREAK_LOSS;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("streak", "streak_loss");
        parameter.addValue("id", player.getId());

        return namedParameterJdbcTemplate.queryForObject(query,parameter, Integer.class);
    }
}