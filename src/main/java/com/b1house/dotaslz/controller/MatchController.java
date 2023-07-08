package com.b1house.dotaslz.controller;

import com.b1house.dotaslz.ExternalApi.PlayerInfo;
import com.b1house.dotaslz.ExternalApi.StratzServiceGraphQL;
import com.b1house.dotaslz.model.Match;
import com.b1house.dotaslz.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchController {
    private MatchService matchService;
    private StratzServiceGraphQL stratzServiceGraphQL;

    public MatchController(MatchService matchService, StratzServiceGraphQL stratzServiceGraphQL){
        this.matchService = matchService;
        this.stratzServiceGraphQL = stratzServiceGraphQL;
    }

//    @GetMapping("/{idDota}")
//    ResponseEntity<Match> getMatchByIdDota(@PathVariable String idDota){
//        return ResponseEntity.ok(matchService.getMatchByIdDota(idDota));
//    }

    @GetMapping
    ResponseEntity<List<Match>> getAll(){
        return ResponseEntity.ok(matchService.getAll());
    }

    @GetMapping("/recent-by-player")
    ResponseEntity<List<Match>> getRecentMatchesByAllPlayers(){
        return ResponseEntity.ok(matchService.getRecentMatchesByAllPlayers());
    }

    @GetMapping("/recent")
    ResponseEntity<List<Match>> getRecentMatches(){
        return ResponseEntity.ok(matchService.getRecentMatches());
    }

    @GetMapping("/test")
    ResponseEntity<String> testGraphQL(){
        stratzServiceGraphQL.saveMatchInfos();
        return ResponseEntity.ok("done");
    }
}
