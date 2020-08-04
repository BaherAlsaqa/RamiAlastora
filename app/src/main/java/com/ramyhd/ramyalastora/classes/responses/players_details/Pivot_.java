
package com.ramyhd.ramyalastora.classes.responses.players_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pivot_ {

    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("league_active_id")
    @Expose
    private Integer leagueActiveId;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getLeagueActiveId() {
        return leagueActiveId;
    }

    public void setLeagueActiveId(Integer leagueActiveId) {
        this.leagueActiveId = leagueActiveId;
    }

}
