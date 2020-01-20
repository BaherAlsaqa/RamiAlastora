
package com.ramialastora.ramialastora.classes.responses.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {

    @SerializedName("players")
    @Expose
    private ArrayList<Player> players = null;
    @SerializedName("teams")
    @Expose
    private ArrayList<Team> teams = null;
    @SerializedName("leagues")
    @Expose
    private ArrayList<League> leagues = null;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public ArrayList<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(ArrayList<League> leagues) {
        this.leagues = leagues;
    }

}
