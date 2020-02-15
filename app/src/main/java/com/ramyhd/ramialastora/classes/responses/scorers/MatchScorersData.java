
package com.ramyhd.ramialastora.classes.responses.scorers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MatchScorersData {

    @SerializedName("player_id")
    @Expose
    private Integer playerId;
    @SerializedName("to_team")
    @Expose
    private Integer toTeam;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("totalGoal")
    @Expose
    private Integer totalGoal;
    @SerializedName("team_name")
    @Expose
    private String teamName;
    @SerializedName("countPenaltys")
    @Expose
    private Integer countPenaltys;
    @SerializedName("countInPenaltys")
    @Expose
    private Integer countInPenaltys;
    @SerializedName("cards")
    @Expose
    private ArrayList<String> cards = null;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getToTeam() {
        return toTeam;
    }

    public void setToTeam(Integer toTeam) {
        this.toTeam = toTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getTotalGoal() {
        return totalGoal;
    }

    public void setTotalGoal(Integer totalGoal) {
        this.totalGoal = totalGoal;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getCountPenaltys() {
        return countPenaltys;
    }

    public void setCountPenaltys(Integer countPenaltys) {
        this.countPenaltys = countPenaltys;
    }

    public Integer getCountInPenaltys() {
        return countInPenaltys;
    }

    public void setCountInPenaltys(Integer countInPenaltys) {
        this.countInPenaltys = countInPenaltys;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }
}
