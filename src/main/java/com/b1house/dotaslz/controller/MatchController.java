package com.b1house.dotaslz.controller;

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

    public MatchController(MatchService matchService){
        this.matchService = matchService;
    }

    @GetMapping("/{idDota}")
    ResponseEntity<Match> getMatchByIdDota(@PathVariable String idDota){
        return ResponseEntity.ok(matchService.getMatchByIdDota(idDota));
    }

    @GetMapping("/recent")
    ResponseEntity<List<Match>> getRecentMatchesByAllPlayers(){
        return ResponseEntity.ok(matchService.getRecentMatchesByAllPlayers());
    }
}
