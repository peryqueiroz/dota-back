package com.b1house.dotaslz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public Player(){}

    public Player(Integer id){
        this.id = id;
    }
    public Player(Integer id, String idDota, String nome, String nick, String avatar) {
        this.id = id;
        this.idDota = idDota;
        this.nome = nome;
        this.nick = nick;
        this.avatar = avatar;
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

}

