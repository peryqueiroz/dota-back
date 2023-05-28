package com.b1house.dotaslz.controller;

import com.b1house.dotaslz.dto.RankingPlayer;
import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
import com.b1house.dotaslz.service.MatchService;
import com.b1house.dotaslz.service.PlayerService;
import com.b1house.dotaslz.service.SeasonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/season")
public class SeasonController {
    private SeasonService seasonService;
    private PlayerService playerService;
    private MatchService matchService;

    public SeasonController(SeasonService seasonService, PlayerService playerService, MatchService matchService){
        this.seasonService = seasonService;
        this.playerService = playerService;
        this.matchService = matchService;
    }
    @GetMapping("/activated")
    ResponseEntity<Season> getSeasonActivated(){
        return ResponseEntity.ok(seasonService.getSeasonActivated());
    }

    @PostMapping("/player/saveScore/{playerIdDota}")
    ResponseEntity saveScorePlayer(@RequestParam Boolean win, @RequestParam String matchIdDota,
                                   @RequestParam Boolean isParty,
                                   @RequestParam String heroUrl,
                                   @RequestParam LocalDateTime date,
                                   @RequestParam Integer kills,
                                   @RequestParam Integer deaths,
                                   @RequestParam Integer assists,
                                   @PathVariable String playerIdDota){
        Player player = playerService.getPlayerByIdDota(playerIdDota);
        Season season = seasonService.getSeasonActivated();

        Match match = new Match();
        match.setPlayer(player);
        match.setIsParty(isParty);
        match.setWin(win);
        match.setHeroUrl(heroUrl);
        match.setDate(date);
        match.setIdDota(matchIdDota);
        match.setKills(kills);
        match.setAssists(assists);
        match.setDeaths(deaths);

        Integer idMatch = matchService.saveMatch(match);
        match.setId(idMatch);

        seasonService.saveScoreSeasonPlayer(player, season, match);

        return ResponseEntity.ok().body("Match saved");
    }

    @GetMapping("/ranking")
    ResponseEntity<List<RankingPlayer>> getCurrentRankingOnActivatedSeason(){
        return ResponseEntity.ok(seasonService.getCurrentRankingOnActivatedSeason());
    }

    @PutMapping("/match")
    ResponseEntity<String> updateGameModeOfMatchOnSeason(@RequestParam String idMatch, @RequestParam List<String> playersIdDota,
                                                         @RequestParam GameMode gameMode, @RequestParam Boolean isWin){
        try{
            seasonService.updateGameModeOfMatchAndScoreOnSeason(idMatch, playersIdDota, gameMode, isWin);
            return ResponseEntity.ok("ok");
        }
        catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }
}