package com.ramialastora.ramialastora.classes;

public class Match {
    public int imageTeam1;
    public int imageTeam2;
    public String nameTeam1;
    public String nameTeam2;
    public String time;
    public int resultTeam1;
    public int resultTeam2;
    public String matchStatus;

    public Match(int imageTeam1, int imageTeam2, String nameTeam1, String nameTeam2, String time, int resultTeam1, int resultTeam2, String matchStatus) {
        this.imageTeam1 = imageTeam1;
        this.imageTeam2 = imageTeam2;
        this.nameTeam1 = nameTeam1;
        this.nameTeam2 = nameTeam2;
        this.time = time;
        this.resultTeam1 = resultTeam1;
        this.resultTeam2 = resultTeam2;
        this.matchStatus = matchStatus;
    }

    public int getImageTeam1() {
        return imageTeam1;
    }

    public void setImageTeam1(int imageTeam1) {
        this.imageTeam1 = imageTeam1;
    }

    public int getImageTeam2() {
        return imageTeam2;
    }

    public void setImageTeam2(int imageTeam2) {
        this.imageTeam2 = imageTeam2;
    }

    public String getNameTeam1() {
        return nameTeam1;
    }

    public void setNameTeam1(String nameTeam1) {
        this.nameTeam1 = nameTeam1;
    }

    public String getNameTeam2() {
        return nameTeam2;
    }

    public void setNameTeam2(String nameTeam2) {
        this.nameTeam2 = nameTeam2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getResultTeam1() {
        return resultTeam1;
    }

    public void setResultTeam1(int resultTeam1) {
        this.resultTeam1 = resultTeam1;
    }

    public int getResultTeam2() {
        return resultTeam2;
    }

    public void setResultTeam2(int resultTeam2) {
        this.resultTeam2 = resultTeam2;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }
}
