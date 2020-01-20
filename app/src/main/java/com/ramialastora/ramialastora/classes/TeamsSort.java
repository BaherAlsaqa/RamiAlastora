package com.ramialastora.ramialastora.classes;

public class TeamsSort {
    public int sort;
    public int image;
    public String teamName;
    public int played;
    public int won;
    public int drew;
    public int loser;
    public int scored;
    public int ownNet;
    public int rest;
    public int points;

    public TeamsSort(int sort, int image, String teamName, int played, int won, int drew, int loser, int scored, int ownNet, int rest, int points) {
        this.sort = sort;
        this.image = image;
        this.teamName = teamName;
        this.played = played;
        this.won = won;
        this.drew = drew;
        this.loser = loser;
        this.scored = scored;
        this.ownNet = ownNet;
        this.rest = rest;
        this.points = points;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getDrew() {
        return drew;
    }

    public void setDrew(int drew) {
        this.drew = drew;
    }

    public int getLoser() {
        return loser;
    }

    public void setLoser(int loser) {
        this.loser = loser;
    }

    public int getScored() {
        return scored;
    }

    public void setScored(int scored) {
        this.scored = scored;
    }

    public int getOwnNet() {
        return ownNet;
    }

    public void setOwnNet(int ownNet) {
        this.ownNet = ownNet;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
