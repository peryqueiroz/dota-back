package com.b1house.dotaslz.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Match {
    private Integer id;
    private Integer idDota;
    private Player player;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Integer heroId;
    private LocalDateTime date;
}
