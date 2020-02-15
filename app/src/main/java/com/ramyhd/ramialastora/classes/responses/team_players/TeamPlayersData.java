
package com.ramyhd.ramialastora.classes.responses.team_players;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TeamPlayersData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("player_id")
    @Expose
    private Integer playerId;
    @SerializedName("to_team")
    @Expose
    private Integer toTeam;
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
    @SerializedName("totalGoal")
    @Expose
    private Integer totalGoal;
    @SerializedName("countPenaltys")
    @Expose
    private Integer countPenaltys;
    @SerializedName("countInPenaltys")
    @Expose
    private Integer countInPenaltys;
    @SerializedName("position")
    @Expose
    private Position position;
    @SerializedName("cards")
    @Expose
    private ArrayList<String> cards = null;

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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }

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
}
