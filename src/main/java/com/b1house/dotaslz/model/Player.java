package com.b1house.dotaslz.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;


@Builder
@Table("players")
public class Player {
    @Id
    private Integer id;
    @Column("id_dota")
    private String idDota;
    @Column("nome")
    private String nome;
    @Column("nick")
    private String nick;
    @Column("avatar")
    private String avatar;
    @MappedCollection(idColumn = "player_id", keyColumn = "id")
    private List<Match> matches;
    @Column("is_private")
    private Boolean isPrivate;
    private Integer mmr;
    @Column("is_main")
    private Boolean isMain;
    public Player(){}

    public Player(Integer id){
        this.id = id;
    }
    public Player(Integer id, String idDota, String nome, String nick, String avatar, Boolean isPrivate, Integer mmr, Boolean isMain) {
        this.id = id;
        this.idDota = idDota;
        this.nome = nome;
        this.nick = nick;
        this.avatar = avatar;
        this.isPrivate = isPrivate;
        this.mmr = mmr;
        this.isMain =isMain;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdDota() {
        return idDota;
    }

    public void setIdDota(String idDota) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Integer getMmr() {
        return mmr;
    }

    public void setMmr(Integer mmr) {
        this.mmr = mmr;
    }

    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }
}

