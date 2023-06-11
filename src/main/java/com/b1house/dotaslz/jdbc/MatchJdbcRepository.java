package com.b1house.dotaslz.jdbc;

import com.b1house.dotaslz.dto.MatchQuantityPlayers;
import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.repository.MatchRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Component
public class MatchJdbcRepository implements MatchRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MatchJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final String FIND_MATCH = """
        SELECT m.id AS match_id, m.id_dota AS match_id_dota, m.kills, m.deaths, m.assists,
        m.hero_url, m.date, m.win, m.is_party, p.id as player_id, p.id_dota as player_id_dota, p.nome, p.nick, p.avatar, p.is_private
        
        FROM matches m
        INNER JOIN players p on m.player_id = p.id 
        """;

    final String INSERT_MATCH = """
        INSERT INTO matches (id_dota, player_id, kills, deaths, assists, hero_url, date, win, is_party)
        VALUES (:id_dota, :player_id, :kills, :deaths, :assists, :hero_url, :date, :win, :is_party)
        """;

    final String UPDATE_GAME_MODE= """
        UPDATE matches SET is_party = :is_party
        WHERE id = :id
        """;

    final String FIND_MULTIPLE_PLAYERS_BY_MATCH = """
        select id_dota, count(player_id) as quantity, win from matches where is_party = false 
        and date > '2023-05-21 23:59:59.000000' group by id_dota, is_party, win having count(player_id) > 2;
        """;

    @Override
    public Match findMatchByIdDota(Player player, String idDota) {
        String query = FIND_MATCH;
        query += " WHERE m.id_dota = :id_dota AND p.id = :player_id";

        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("id_dota", idDota);
        parameter.addValue("player_id", player.getId());

        return namedParameterJdbcTemplate.queryForObject(query, parameter, this::rowMapper);
    }

    @Override
    public List<Match> findRecentMatchByPlayer(Integer playerId) {
        String query = FIND_MATCH;
        query += " where player_id = :player_id order by date desc limit 3";
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("player_id", playerId);

        return namedParameterJdbcTemplate.query(query, parameter, (rs, rowNum) -> {
            Match match = new Match();
            match.setId(rs.getInt("match_id"));
            match.setIdDota(rs.getString("match_id_dota"));
            match.setKills(rs.getInt("kills"));
            match.setAssists(rs.getInt("assists"));
            match.setDeaths(rs.getInt("deaths"));
            match.setHeroUrl(rs.getString("hero_url"));
            match.setWin(rs.getBoolean("win"));
            match.setIsParty(rs.getBoolean("is_party"));

            Timestamp timestamp = rs.getTimestamp("date");
            match.setDate(timestamp.toLocalDateTime());
            return match;
        });
    }

    @Override
    public Integer saveMatch(Match match) {
        String query = INSERT_MATCH;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        parameter.addValue("id_dota",match.getIdDota());
        parameter.addValue("player_id",match.getPlayer().getId());
        parameter.addValue("kills",match.getKills());
        parameter.addValue("deaths",match.getDeaths());
        parameter.addValue("assists",match.getAssists());
        parameter.addValue("hero_url",match.getHeroUrl());
        parameter.addValue("date",match.getDate());
        parameter.addValue("win", match.getWin());
        parameter.addValue("is_party", match.getIsParty());

        namedParameterJdbcTemplate.update(query, parameter, generatedKeyHolder, new String[]{"id"});

        return Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();
    }

    @Override
    public void updateGameMode(Integer matchId, GameMode gameMode) {
        String query = UPDATE_GAME_MODE;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("is_party", gameMode == GameMode.PARTY);
        parameter.addValue("id", matchId);
        namedParameterJdbcTemplate.update(query,parameter);
    }

    @Override
    public List<Match> findAll() {
        String query = FIND_MATCH;
        query += " ORDER BY date desc ";

        return namedParameterJdbcTemplate.query(query, this::rowMapper);
    }

    @Override
    public List<Match> findRecentMatches() {
        String query = FIND_MATCH;
        query += " order by date desc limit 20";

        return namedParameterJdbcTemplate.query(query, this::rowMapper);
    }

    @Override
    public List<MatchQuantityPlayers> findMultiplePlayersByMatch() {
        return namedParameterJdbcTemplate.query(FIND_MULTIPLE_PLAYERS_BY_MATCH,(rs, rowNum) -> {
            MatchQuantityPlayers matchQuantityPlayers = new MatchQuantityPlayers();
            matchQuantityPlayers.setIdDota(rs.getString("id_dota"));
            matchQuantityPlayers.setQuantity(rs.getInt("quantity"));
            matchQuantityPlayers.setWin(rs.getBoolean("win"));

            return matchQuantityPlayers;
        });
    }

    private Match rowMapper(ResultSet rs, int rowNum) throws SQLException {
        Match match = new Match();
        match.setId(rs.getInt("match_id"));
        match.setIdDota(rs.getString("match_id_dota"));
        match.setKills(rs.getInt("kills"));
        match.setAssists(rs.getInt("assists"));
        match.setDeaths(rs.getInt("deaths"));
        match.setHeroUrl(rs.getString("hero_url"));
        match.setWin(rs.getBoolean("win"));
        match.setIsParty(rs.getBoolean("is_party"));

        Timestamp timestamp = rs.getTimestamp("date");
        match.setDate(timestamp.toLocalDateTime());

        Player player = new Player();
        player.setId(rs.getInt("player_id"));
        player.setIdDota(rs.getString("player_id_dota"));
        player.setNome(rs.getString("nome"));
        player.setNick(rs.getString("nick"));
        player.setAvatar(rs.getString("avatar"));
        player.setIsPrivate(rs.getBoolean("is_private"));
        match.setPlayer(player);

        return match;
    }
}
