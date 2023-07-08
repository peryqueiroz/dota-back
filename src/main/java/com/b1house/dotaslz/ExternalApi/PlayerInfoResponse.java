package com.b1house.dotaslz.ExternalApi;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerInfoResponse {
    @JsonProperty("data")
    private MatchData data;

    public MatchData getData() {
        return data;
    }
    public void setData(MatchData data) {
        this.data = data;
    }
}
