
package com.ramyhd.ramyalastora.classes.responses.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ramyhd.ramyalastora.classes.responses.players_details.ParticipatingLeague;

import java.util.ArrayList;

public class Team {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("admin_id")
    @Expose
    private Integer adminId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("favorite")
    @Expose
    private int favorite;
    @SerializedName("country")
    @Expose
    private Country country;
    @SerializedName("participating_leagues")
    @Expose
    private ArrayList<ParticipatingLeague> participatingLeagueArrayList = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public ArrayList<ParticipatingLeague> getParticipatingLeagueArrayList() {
        return participatingLeagueArrayList;
    }

    public void setParticipatingLeagueArrayList(ArrayList<ParticipatingLeague> participatingLeagueArrayList) {
        this.participatingLeagueArrayList = participatingLeagueArrayList;
    }
}
