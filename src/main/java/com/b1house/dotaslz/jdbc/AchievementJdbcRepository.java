package com.b1house.dotaslz.jdbc;

import com.b1house.dotaslz.model.Achievement;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.AchievementRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Component
public class AchievementJdbcRepository implements AchievementRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AchievementJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final String UPDATE_ACHIEVEMENT = """
        UPDATE achievement SET quantity = :quantity , 
        player_id = :player_id , updated_at = :updated_at
        WHERE type = :type
        """;

    final String GET_QUANTITY = """
        SELECT quantity from achievement
        WHERE type = :type;
        """;

    final String FIND_ALL = """
        SELECT a.id,a.type, a.quantity, a.updated_at,  p.id as player_id, p.id_dota as player_id_dota, p.nome, p.nick, p.avatar, p.is_private 
        FROM achievement a
        INNER JOIN players p on p.id = a.player_id
        """;

    @Override
    public void edit(Achievement achievement) {
        String query = UPDATE_ACHIEVEMENT;
        MapSqlParameterSource parameter = new MapSqlParameterSource();

        parameter.addValue("quantity", achievement.getQuantity());
        parameter.addValue("player_id", achievement.getPlayer().getId());
        parameter.addValue("updated_at", achievement.getUpdatedAt());
        parameter.addValue("type", achievement.getType());

        namedParameterJdbcTemplate.update(query, parameter);
    }

    @Override
    public BigDecimal getQuantitiyByType(String type) {
        String query = GET_QUANTITY;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("type", type);

        return namedParameterJdbcTemplate.queryForObject(query, parameter, BigDecimal.class);
    }

    @Override
    public List<Achievement> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL, (rs, rowNum) -> {
            Achievement achievement = new Achievement();
            achievement.setId(rs.getInt("id"));
            achievement.setType(rs.getString("type"));
            achievement.setQuantity(rs.getBigDecimal("quantity"));

            Player player = new Player();
            player.setId(rs.getInt("player_id"));
            player.setIdDota(rs.getString("player_id_dota"));
            player.setNome(rs.getString("nome"));
            player.setNick(rs.getString("nick"));
            player.setAvatar(rs.getString("avatar"));
            player.setIsPrivate(rs.getBoolean("is_private"));
            achievement.setPlayer(player);

            return achievement;
        });
    }
}
