package com.b1house.dotaslz.controller;

import com.b1house.dotaslz.dto.RankingPlayer;
import com.b1house.dotaslz.enums.GameMode;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.model.Season;
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

import java.util.List;

@RestController
@RequestMapping("/season")
public class SeasonController {
    private SeasonService seasonService;
    private PlayerService playerService;

    public SeasonController(SeasonService seasonService, PlayerService playerService){
        this.seasonService = seasonService;
        this.playerService = playerService;
    }

    @GetMapping("/activated")
    ResponseEntity<Season> getSeasonActivated(){
        return ResponseEntity.ok(seasonService.getSeasonActivated());
    }

//    @PostMapping("/player/saveScore/{playerId}")
//    ResponseEntity saveScorePlayer(@RequestParam Integer score, @RequestParam Integer matchId, @PathVariable Integer playerId){
//        Player player = playerService.getPlayerById(playerId);
//        Season season = seasonService.getSeasonActivated();
//        Match match = new Match();
//        match.setId(matchId);
//
//        if(season.getId() != null && player.getIsMain()){
//            seasonService.saveScoreSeasonPlayer(player, season, match, score);
//            return ResponseEntity.ok().body("Score of " + player.getNome()+" saved - "+score+" points");
//        }
//        else{
//            System.out.println("None Season activated or Player is smurf");
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/ranking")
    ResponseEntity<List<RankingPlayer>> getCurrentRankingOnActivatedSeason(){
        return ResponseEntity.ok(seasonService.getCurrentRankingOnActivatedSeason());
    }

//    @PutMapping("/match")
//    ResponseEntity<String> updateGameModeOfMatchOnSeason(@RequestParam String idMatch, @RequestParam List<String> playersIdDota,
//                                                         @RequestParam GameMode gameMode, @RequestParam Boolean isWin){
//        try{
//            seasonService.updateGameModeOfMatchAndScoreOnSeason(idMatch, playersIdDota, gameMode, isWin);
//            return ResponseEntity.ok("ok");
//        }
//        catch (Exception e){
//            return ResponseEntity.ok(e.getMessage());
//        }
//    }
}