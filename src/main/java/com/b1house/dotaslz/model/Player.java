package com.b1house.dotaslz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder

public class Player {
    private Integer id;
    private Integer idDota;
    private String nome;
    private String nick;

    public Player(){}

    public Player(Integer id, Integer idDota, String nome, String nick) {
        this.id = id;
        this.idDota = idDota;
        this.nome = nome;
        this.nick = nick;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdDota() {
        return idDota;
    }

    public void setIdDota(Integer idDota) {
        this.idDota = idDota;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

}

