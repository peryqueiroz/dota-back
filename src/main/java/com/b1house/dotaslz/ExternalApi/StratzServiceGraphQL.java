package com.b1house.dotaslz.ExternalApi;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.service.AchievementService;
import com.b1house.dotaslz.service.MatchService;
import com.b1house.dotaslz.service.PlayerService;
import com.b1house.dotaslz.service.SeasonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class StratzServiceGraphQL {
    private final RestTemplate restTemplate;
    private final String bearerToken;
    private final PlayerService playerService;
    private final MatchService matchService;
    private final SeasonService seasonService;

    private final AchievementService achievementService;
    private static final String API_URL = "https://api.stratz.com/graphql";

    public StratzServiceGraphQL(RestTemplate restTemplate, @Value("${external.api.token}") String bearerToken, PlayerService playerService,
                         MatchService matchService, SeasonService seasonService, AchievementService achievementService) {
        this.restTemplate = restTemplate;
        this.bearerToken = bearerToken;
        this.playerService = playerService;
        this.matchService = matchService;
        this.seasonService = seasonService;
        this.achievementService = achievementService;
    }

    public void saveMatchInfos(){
        List<Match> matches = matchService.getAll();

        matches.forEach(match -> {
            System.out.println("id dota: ");
            System.out.println(match.getIdDota());
            System.out.println("player: ");
            System.out.println(match.getPlayer().getNome());
            playerInfo(match.getIdDota(), match.getPlayer().getIdDota(), match.getId());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public PlayerInfo playerInfo(String matchIdDota, String playerId, Integer matchId){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String query = String.format("{ \"query\": \"query { match(id: %s) { players(steamAccountId: %s) { heroDamage, towerDamage, heroHealing, imp, award } } }\" }", matchIdDota, playerId);

        HttpEntity<String> requestEntity = new HttpEntity<>(query, headers);

        ResponseEntity<PlayerInfoResponse> responseEntity = restTemplate.exchange(
            API_URL,
            HttpMethod.POST,
            requestEntity,
            PlayerInfoResponse.class
        );

        MatchData matchData = responseEntity.getBody().getData();
        if (matchData != null && matchData.getMatch() != null && matchData.getMatch().getPlayers() != null && matchData.getMatch().getPlayers().length > 0) {

            matchService.updateMatchInfos(matchData.getMatch().getPlayers()[0].getHeroDamage(),
                matchData.getMatch().getPlayers()[0].getTowerDamage(), matchData.getMatch().getPlayers()[0].getHeroHealing(), matchData.getMatch().getPlayers()[0].getImp(), matchData.getMatch().getPlayers()[0].getAward(),matchId); ;
        }
        return null;
    }


}
