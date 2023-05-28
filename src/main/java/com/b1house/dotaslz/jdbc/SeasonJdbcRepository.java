package com.b1house.dotaslz.jdbc;

import com.b1house.dotaslz.dto.RankingPlayer;
import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
import com.b1house.dotaslz.repository.SeasonRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

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
        INSERT INTO season_players (player_id, season_id, score, match_id, updated_at) VALUES (:player_id, :season_id, :score, :match_id, :updated_at)        
        """;

    final String FIND_CURRENT_RANKING =  """
    SELECT p.nome,p.nick,SUM(sp.score) as score,
       count(CASE WHEN m.win = true AND m.is_party = true THEN 1 END) as winParty,
       count(CASE WHEN m.win = false AND m.is_party = true THEN 1 END) as lossParty,
       count(CASE WHEN m.win = true AND m.is_party = false THEN 1 END) as winSolo,
       count(CASE WHEN m.win = false AND m.is_party = false THEN 1 END) as lossSolo
        FROM season_players sp
        INNER JOIN players p on p.id = sp.player_id
        INNER JOIN matches m on m.id = sp.match_id
        INNER JOIN season s on sp.season_id = s.id
        WHERE s.actived = true
        group by p.nome, p.nick;
""";

    final String UPDATE_GAME_MODE = """
        UPDATE season_players SET score = :score
        WHERE match_id = :match_id and player_id = :player_id ;
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
    public void saveScoreSeasonPlayer(Player player, Season season, Match match) {
        String query = INSERT_SCORE;
        MapSqlParameterSource parameter = new MapSqlParameterSource();

        Integer score = match.getIsParty()? 20:30;
        score = match.getWin()? score : (score * -1);

        parameter.addValue("player_id", player.getId());
        parameter.addValue("season_id", season.getId());
        parameter.addValue("score", score);
        parameter.addValue("match_id", match.getId());
        parameter.addValue("updated_at", match.getDate());

        namedParameterJdbcTemplate.update(query,parameter);
    }

    @Override
    public List<RankingPlayer> getCurrentRankingOnActivatedSeason() {
        String query = FIND_CURRENT_RANKING;
        return namedParameterJdbcTemplate.query(query, (rs, rowNum) -> {
            RankingPlayer rankingPlayer = new RankingPlayer();
            rankingPlayer.setName(rs.getString("nome"));
            rankingPlayer.setNick(rs.getString("nick"));
            rankingPlayer.setScore(rs.getInt("score"));
            rankingPlayer.setWinParty(rs.getInt("winParty"));
            rankingPlayer.setWinSolo(rs.getInt("winSolo"));
            rankingPlayer.setLossParty(rs.getInt("lossParty"));
            rankingPlayer.setLossSolo(rs.getInt("lossSolo"));
            rankingPlayer.setTotalMatches(rankingPlayer.getLossParty()+rankingPlayer.getLossSolo()+rankingPlayer.getWinParty()+rankingPlayer.getWinSolo());
            rankingPlayer.setIsPlayerEligible(rankingPlayer.getTotalMatches() >= 20);

            return rankingPlayer;
        });
    }

    @Override
    public void updateScoreOnSeason(Integer idMatch, Integer playerId, GameMode gameMode, Boolean isWin) {
        String query = UPDATE_GAME_MODE;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        Integer score = gameMode.getScore();
        if (!isWin){
            score = score * -1;
        }
        parameter.addValue("score", score);
        parameter.addValue("match_id", idMatch);
        parameter.addValue("player_id", playerId);

        namedParameterJdbcTemplate.update(query, parameter);
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