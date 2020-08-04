
package com.ramyhd.ramyalastora.classes.responses.participating_teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParticipatingTeamsData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("ranking")
    @Expose
    private Integer ranking;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("favorite")
    @Expose
    private Integer favorite;
    @SerializedName("tema_id")
    @Expose
    private Integer temaId;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("count_matches_played")
    @Expose
    private Integer countMatchesPlayed;
    @SerializedName("count_matches_won")
    @Expose
    private Integer countMatchesWon;
    @SerializedName("count_matches_draw")
    @Expose
    private Integer countMatchesDraw;
    @SerializedName("count_matches_lost")
    @Expose
    private Integer countMatchesLost;
    @SerializedName("count_goals")
    @Expose
    private Integer countGoals;
    @SerializedName("count_goals_in")
    @Expose
    private Integer countGoalsIn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getTemaId() {
        return temaId;
    }

    public void setTemaId(Integer temaId) {
        this.temaId = temaId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getCountMatchesPlayed() {
        return countMatchesPlayed;
    }

    public void setCountMatchesPlayed(Integer countMatchesPlayed) {
        this.countMatchesPlayed = countMatchesPlayed;
    }

    public Integer getCountMatchesWon() {
        return countMatchesWon;
    }

    public void setCountMatchesWon(Integer countMatchesWon) {
        this.countMatchesWon = countMatchesWon;
    }

    public Integer getCountMatchesDraw() {
        return countMatchesDraw;
    }

    public void setCountMatchesDraw(Integer countMatchesDraw) {
        this.countMatchesDraw = countMatchesDraw;
    }

    public Integer getCountMatchesLost() {
        return countMatchesLost;
    }

    public void setCountMatchesLost(Integer countMatchesLost) {
        this.countMatchesLost = countMatchesLost;
    }

    public Integer getCountGoals() {
        return countGoals;
    }

    public void setCountGoals(Integer countGoals) {
        this.countGoals = countGoals;
    }

    public Integer getCountGoalsIn() {
        return countGoalsIn;
    }

    public void setCountGoalsIn(Integer countGoalsIn) {
        this.countGoalsIn = countGoalsIn;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }
}
