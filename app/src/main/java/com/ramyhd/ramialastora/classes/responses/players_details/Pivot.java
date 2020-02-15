
package com.ramyhd.ramialastora.classes.responses.players_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pivot {

    @SerializedName("player_id")
    @Expose
    private Integer playerId;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

}
