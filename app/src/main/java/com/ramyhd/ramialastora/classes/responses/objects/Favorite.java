package com.ramyhd.ramialastora.classes.responses.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ramyhd.ramialastora.classes.responses.players_details.ParticipatingLeague;

import java.util.ArrayList;

public class Favorite {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("team_id")
    @Expose
    private int teamId;
    @SerializedName("favorite")
    @Expose
    private int favorite;
    @SerializedName("participating_leagues")
    @Expose
    private ArrayList<ParticipatingLeague> participatingLeagues;

    public Favorite() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public ArrayList<ParticipatingLeague> getParticipatingLeagues() {
        return participatingLeagues;
    }

    public void setParticipatingLeagues(ArrayList<ParticipatingLeague> participatingLeagues) {
        this.participatingLeagues = participatingLeagues;
    }
}
