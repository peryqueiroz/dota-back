package com.b1house.dotaslz.ExternalApi;

import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
import com.b1house.dotaslz.service.AchievementService;
import com.b1house.dotaslz.service.MatchService;
import com.b1house.dotaslz.service.PlayerService;
import com.b1house.dotaslz.service.SeasonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
    private final SeasonService seasonService;

    private final AchievementService achievementService;

    public StratzService(RestTemplate restTemplate, @Value("${external.api.token}") String bearerToken, PlayerService playerService,
                         MatchService matchService, SeasonService seasonService, AchievementService achievementService) {
        this.restTemplate = restTemplate;
        this.bearerToken = bearerToken;
        this.playerService = playerService;
        this.matchService = matchService;
        this.seasonService = seasonService;
        this.achievementService = achievementService;
    }
//    @Scheduled(fixedRate = 300000)
    public void scheduleFetchAndSaveNewMatches(){
        List<Player> players = playerService.getAllPlayers();

        players.forEach( player->{
            fetchDataFromApiByPlayer(player);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });
    }
//    @Scheduled(fixedRate = 1860000)
    public void scheduleFetchAndSaveInfoPlayers(){
        List<Player> players = playerService.getAllPlayers();

        players.forEach( player->{
            fetchInfoPlayer(player);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });
    }
    private void fetchDataFromApiByPlayer(Player player){
        try {
            String url = "https://api.stratz.com/api/v1/Player/" + player.getIdDota() + "/matches";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(bearerToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ParameterizedTypeReference<List<HashMap>> responseType = new ParameterizedTypeReference<List<HashMap>>() {
            };
            List<HashMap> response = restTemplate.exchange(requestEntity, responseType).getBody();

            String matchId = response.get(0).get("id").toString();

            if (isNewMatch(player, matchId)) {

                System.out.println(player.getNome() +" has a new match - "+ matchId);
                Match match = fillMatchFromApiResponse(response, player);
                Season season = new Season();
                try{
                    season = getSeasonActivated();
                } catch (Exception e){
                    System.out.println("Match out of Season");
                }

                Integer matchIdSaved = saveMatch(match);
                match.setId(matchIdSaved);

                if(season.getId() != null && player.getIsMain()){
                    System.out.println(match.getIsParty()? "Party ":"Solo " + "match "+ match.getIdDota()+" of " +player.getNome()+" on Season " + season.getVersion() + " detected..");
                    System.out.print("Result - ");
                    System.out.println(match.getWin()? "Win" : "Loss");

                    updateScorePlayer(player, match, season);
                    updateAchievement(player, match);
                }
            }
        } catch (Exception e){
            if(e.getMessage().contains("403") && !player.getIsPrivate()){
                player.setIsPrivate(true);
                playerService.updateIsPrivate(player);
            }
            System.out.println("Player: " + player.getNome());
            System.out.println("Error: " + e.getMessage());
        }
    }
    private Integer saveMatch(Match match){
        try {
            return matchService.saveMatch(match);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error saving a match "+match.getIdDota()+" of "+ match.getPlayer().getNome());
        }
    }
    private Match fillMatchFromApiResponse(List<HashMap> response, Player player){
        Match match = new Match();

        match.setIdDota(response.get(0).get("id").toString());
        match.setPlayer(player);
        List playersList = (List) response.get(0).get("players");
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

        Boolean isParty = firstMatch.containsKey("partyId");
        match.setIsParty(isParty);

        try{
            String heroUrl = getHeroUrlById(Integer.parseInt(firstMatch.get("heroId").toString()));
            match.setHeroUrl(heroUrl);
        } catch (Exception e){
            System.out.println("Error getting heroUrl from API");
        }

        return match;
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
        Boolean playerIsPrivate = Boolean.parseBoolean(steamAccount.get("isAnonymous").toString());

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

        if(playerIsPrivate != player.getIsPrivate()){
            System.out.println("Updating isPrivate of "+player.getNome() +" from "+ player.getIsPrivate().toString() +" to "+
                playerIsPrivate);

            player.setIsPrivate(playerIsPrivate);
            playerService.updateIsPrivate(player);
        }
    }

    private Boolean isNewMatch(Player player, String matchId){
        try{
            Match match = matchService.getMatchByIdDota(player, matchId);
            return match.getId() == null;
        }
        catch (IncorrectResultSizeDataAccessException e){
            if(e.getActualSize() > 0) {
                System.out.println("Error finding new match..");
                System.out.println("Player: "+ player.getNome());
                System.out.println("Match: "+ matchId);
                System.out.println(e.getMessage());
            }
            return true;
        }
    }
    private Season getSeasonActivated(){
        return seasonService.getSeasonActivated();
    }

    private void updateScorePlayer(Player player, Match match, Season season){
        GameMode gameMode = match.getIsParty() ? GameMode.PARTY : GameMode.SOLO;
        Integer scoreFinal = match.getWin() ? gameMode.getScore() : (gameMode.getScore() * -1);

        seasonService.saveScoreSeasonPlayer(player,season,match);
    }

    private void updateAchievement(Player player, Match match){
        achievementService.updateWinLoss(player, match);
//        achievementService.updateKda(player, match);
    }
}
