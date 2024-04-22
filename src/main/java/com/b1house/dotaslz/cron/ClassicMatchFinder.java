package com.b1house.dotaslz.cron;

import com.b1house.dotaslz.dto.MatchQuantityPlayers;
import com.b1house.dotaslz.model.Season;
import com.b1house.dotaslz.service.MatchService;
import com.b1house.dotaslz.service.SeasonService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ClassicMatchFinder {
    private final MatchService matchService;
    private final SeasonService seasonService;

    public ClassicMatchFinder(MatchService matchService, SeasonService seasonService){
        this.matchService = matchService;
        this.seasonService = seasonService;
    }

//    @Scheduled(fixedRate = 43200000)
    public void findMultiplePlayersByMatch(){
        System.out.println("Starting to search classic matches");
        Season season = new Season();
        try{
            season = seasonService.getSeasonActivated();

            if(season.getId() != null){
                List<MatchQuantityPlayers> matchQuantityPlayers = matchService.getMultiplePlayersByMatch(season.getId());
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
        } catch (Exception ignored){

        }

    }
}
