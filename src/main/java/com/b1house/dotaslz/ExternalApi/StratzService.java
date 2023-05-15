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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    @Scheduled(fixedRate = 300000)
    public void scheduleFetchAndSave(){
        List<Player> players = playerService.getAllPlayers();

        players.forEach( player->{
            fetchDataFromApiByPlayer(player);
            fetchInfoPlayer(player);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });
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

            Boolean didRadiantWin = Boolean.parseBoolean(response.get(0).get("didRadiantWin").toString());
            Boolean isRadiant = Boolean.parseBoolean(firstMatch.get("isRadiant").toString());
            Boolean win = didRadiantWin == isRadiant;

            match.setWin(win);

            Instant instant = Instant.ofEpochSecond(Long.parseLong(response.get(0).get("endDateTime").toString()));
            ZoneId zone = ZoneId.of("GMT-3");
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

            match.setDate(localDateTime);

            String heroUrl =  getHeroUrlById(Integer.parseInt(firstMatch.get("heroId").toString()));
            match.setHeroUrl(heroUrl);


            try{
                matchService.saveMatch(match);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println(matchId + " match nao é nova");
        }
    }
    private String getHeroUrlById(Integer id){
        String url = "https://api.stratz.com/api/v1/Hero";
        String baseUrlReturn = "https://api.opendota.com/apps/dota2/images/heroes/";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        ParameterizedTypeReference<HashMap> responseType = new ParameterizedTypeReference<HashMap>() {};
        HashMap response = restTemplate.exchange(requestEntity, responseType).getBody();

        HashMap<String, Object> heroMap = (HashMap<String, Object>) response.get(id.toString());

        String heroName = heroMap.get("shortName").toString();

        return baseUrlReturn + heroName + "_full.png";
    }

    private void fetchInfoPlayer(Player player){
        String url = "https://api.stratz.com/api/v1/Player/"+player.getIdDota();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        ParameterizedTypeReference<HashMap> responseType = new ParameterizedTypeReference<HashMap>() {};
        HashMap response = restTemplate.exchange(requestEntity, responseType).getBody();

        HashMap<String, Object> steamAccount = (HashMap<String, Object>) response.get("steamAccount");

        String avatar = steamAccount.get("avatar").toString();
        String nick = steamAccount.get("name").toString();

        if(!Objects.equals(avatar, player.getAvatar())){
            System.out.println("Updating avatar of "+ player.getNome());

            player.setAvatar(avatar);
            playerService.updateAvatar(player);
        }

        if(!Objects.equals(nick, player.getNick())){
            System.out.println("Updating nick of "+ player.getNome());

            player.setNick(nick);
            playerService.updateNick(player);
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
