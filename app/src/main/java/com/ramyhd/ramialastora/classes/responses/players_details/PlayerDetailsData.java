
package com.ramyhd.ramialastora.classes.responses.players_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlayerDetailsData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("datebirth")
    @Expose
    private String datebirth;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("position_id")
    @Expose
    private Integer positionId;
    @SerializedName("admin_id")
    @Expose
    private Integer adminId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("totalGoal")
    @Expose
    private Integer totalGoal;
    @SerializedName("totalOG")
    @Expose
    private Integer totalOG;
    @SerializedName("penaltiesTrue")
    @Expose
    private Integer penaltiesTrue;
    @SerializedName("penaltiesFalse")
    @Expose
    private Integer penaltiesFalse;
    @SerializedName("card_red")
    @Expose
    private Integer cardRed;
    @SerializedName("card_yellow")
    @Expose
    private Integer cardYellow;
    @SerializedName("teams")
    @Expose
    private ArrayList<Team> teams = null;

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

    public String getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(String datebirth) {
        this.datebirth = datebirth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
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

    public Integer getTotalGoal() {
        return totalGoal;
    }

    public void setTotalGoal(Integer totalGoal) {
        this.totalGoal = totalGoal;
    }

    public Integer getTotalOG() {
        return totalOG;
    }

    public void setTotalOG(Integer totalOG) {
        this.totalOG = totalOG;
    }

    public Integer getPenaltiesTrue() {
        return penaltiesTrue;
    }

    public void setPenaltiesTrue(Integer penaltiesTrue) {
        this.penaltiesTrue = penaltiesTrue;
    }

    public Integer getPenaltiesFalse() {
        return penaltiesFalse;
    }

    public void setPenaltiesFalse(Integer penaltiesFalse) {
        this.penaltiesFalse = penaltiesFalse;
    }

    public Integer getCardRed() {
        return cardRed;
    }

    public void setCardRed(Integer cardRed) {
        this.cardRed = cardRed;
    }

    public Integer getCardYellow() {
        return cardYellow;
    }

    public void setCardYellow(Integer cardYellow) {
        this.cardYellow = cardYellow;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
