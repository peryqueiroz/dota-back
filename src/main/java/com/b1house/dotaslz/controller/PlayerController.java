package com.b1house.dotaslz.controller;

import com.b1house.dotaslz.model.Player;
import com.b1house.dotaslz.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private PlayerService playerService;

    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    @GetMapping
    ResponseEntity<List<Player>> getPlayers(){
        return ResponseEntity.ok( playerService.getAllPlayers());
    }
}
