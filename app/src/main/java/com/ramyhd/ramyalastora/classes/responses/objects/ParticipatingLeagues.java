package com.ramyhd.ramyalastora.classes.responses.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParticipatingLeagues {
    @SerializedName("ranking")
    @Expose
    private int ranking;
    @SerializedName("league_id")
    @Expose
    private int leagueId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
