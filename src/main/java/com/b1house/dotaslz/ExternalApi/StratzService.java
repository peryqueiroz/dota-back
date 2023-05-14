package com.b1house.dotaslz.ExternalApi;

import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.service.MatchService;
import com.b1house.dotaslz.service.PlayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

@Service
public class StratzService {

    private final RestTemplate restTemplate;
    private final String bearerToken;
    private final PlayerService playerService;
    private final MatchService matchService;

    public StratzService(RestTemplate restTemplate, @Value("${external.api.token}") String bearerToken, PlayerService playerService,
                         MatchService matchService) {
        this.restTemplate = restTemplate;
        this.bearerToken = bearerToken;
        this.playerService = playerService;
        this.matchService = matchService;
    }
    @Scheduled(fixedRate = 15000)
    public void scheduleFetchAndSave(){
        List<Player> players = playerService.getAllPlayers();

        players.forEach(this::fetchDataFromApiByPlayer);
    }
    private void fetchDataFromApiByPlayer(Player player){
        String url = "https://api.stratz.com/api/v1/Player/"+player.getIdDota()+"/matches";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        ParameterizedTypeReference<List<HashMap>> responseType = new ParameterizedTypeReference<List<HashMap>>() {};
        List<HashMap> response = restTemplate.exchange(requestEntity, responseType).getBody();

        String matchId = response.get(0).get("id").toString();

        if(isNewMatch(player, matchId)){
            Match match = new Match();
            match.setIdDota(response.get(0).get("id").toString());
            match.setPlayer(player);
            List playersList =  (List) response.get(0).get("players");
            HashMap<String, Object> firstMatch = (HashMap<String, Object>) playersList.get(0);

            match.setKills(Integer.parseInt(firstMatch.get("numKills").toString()));
            match.setDeaths(Integer.parseInt(firstMatch.get("numDeaths").toString()));
            match.setAssists(Integer.parseInt(firstMatch.get("numAssists").toString()));
            match.setHeroId(Integer.parseInt(firstMatch.get("heroId").toString()));

            Instant instant = Instant.ofEpochSecond(Long.parseLong(response.get(0).get("endDateTime").toString()));

            System.out.println("instant "+ instant);
            ZoneId zone = ZoneId.of("GMT-3");
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

            System.out.println("localdate "+ localDateTime.toString());

            match.setDate(localDateTime);

            try{
                matchService.saveMatch(match);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println(matchId + " match nao é nova");
        }
    }

    private Boolean isNewMatch(Player player, String matchId){
        try{
            System.out.println("isNewMatch "+player.getNome() +" - "+ matchId);
            Match match = matchService.getMatchByIdDota(player, matchId);
            System.out.println("isNewMAtchDepois");
            return match.getId() == null;
        }
        catch (Exception e){
            System.out.println("match is newMatch");
            System.out.println(e.getMessage());
            return true;
        }
    }
}
