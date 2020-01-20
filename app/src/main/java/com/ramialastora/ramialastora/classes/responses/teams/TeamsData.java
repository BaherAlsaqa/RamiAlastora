
package com.ramialastora.ramialastora.classes.responses.teams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class TeamsData {

    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("favorite")
    @Expose
    private Integer favorite;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
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

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public static class Comparators {
        //public static final Comparator<Providers> NAME = (Student o1, Student o2) -> o1.name.compareTo(o2.name);
        public static final Comparator<TeamsData> favorite = (TeamsData o2, TeamsData o1) -> Integer.compare(o2.favorite, o1.favorite);
        //public static final Comparator<Providers> NAME_AND_AGE = (Student o1, Student o2) -> NAME.thenComparing(AGE).compare(o1, o2);
    }

}
