package com.b1house.dotaslz.cron;

import com.b1house.dotaslz.dto.MatchQuantityPlayers;
import com.b1house.dotaslz.service.MatchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassicMatchFinder {
    private final MatchService matchService;

    public ClassicMatchFinder(MatchService matchService){
        this.matchService = matchService;
    }

    @Scheduled(fixedRate = 43200000)
    public void findMultiplePlayersByMatch(){
        List<MatchQuantityPlayers> matchQuantityPlayers = matchService.getMultiplePlayersByMatch();

        if(!matchQuantityPlayers.isEmpty()){
            System.out.println("!!!!!! Possible classic matches found !!!!!!!! ");
            matchQuantityPlayers.forEach(match ->{
                System.out.println("Id Dota: " + match.getIdDota());
                System.out.println("Win: " + match.getWin());
                System.out.println("Quantity: " + match.getQuantity());
            });
            System.out.println("___________________________________________________");
        }
    }
}
