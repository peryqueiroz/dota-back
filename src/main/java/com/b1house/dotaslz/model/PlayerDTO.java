package com.b1house.dotaslz.model;

public class PlayerDTO {

    private String avatar;
    private String nick;

    public PlayerDTO() {
    }

    public PlayerDTO(String avatar, String nick) {
        this.avatar = avatar;
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
