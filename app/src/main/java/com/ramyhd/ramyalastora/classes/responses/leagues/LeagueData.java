
package com.ramyhd.ramyalastora.classes.responses.leagues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;

public class LeagueData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("admin_id")
    @Expose
    private Integer adminId;
    @SerializedName("league_type_id")
    @Expose
    private Integer leagueTypeId;
    @SerializedName("group_system")
    @Expose
    private String groupSystem;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("leagues_active")
    @Expose
    private ArrayList<LeaguesActive> leaguesActive = null;

    public static final Comparator<LeagueData> BY_SORTING_ALPHABETICAL =
            (leagueData1, leagueData2) -> leagueData1.leaguesActive.get(0).getSorting().compareTo(leagueData2.leaguesActive.get(0).getSorting());

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getLeagueTypeId() {
        return leagueTypeId;
    }

    public void setLeagueTypeId(Integer leagueTypeId) {
        this.leagueTypeId = leagueTypeId;
    }

    public String getGroupSystem() {
        return groupSystem;
    }

    public void setGroupSystem(String groupSystem) {
        this.groupSystem = groupSystem;
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

    public ArrayList<LeaguesActive> getLeaguesActive() {
        return leaguesActive;
    }

    public void setLeaguesActive(ArrayList<LeaguesActive> leaguesActive) {
        this.leaguesActive = leaguesActive;
    }

}
