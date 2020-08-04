package com.ramyhd.ramyalastora.classes;

public class Scorers {
    public int sort;
    public int image;
    public String name;
    public String center;
    public int yellowCard;
    public int redCard;
    public int goals;
    public int truePelanties;
    public int falsePelanties;

    public Scorers(int sort, int image, String name, String center, int yellowCard, int redCard, int goals, int truePelanties, int falsePelanties) {
        this.sort = sort;
        this.image = image;
        this.name = name;
        this.center = center;
        this.yellowCard = yellowCard;
        this.redCard = redCard;
        this.goals = goals;
        this.truePelanties = truePelanties;
        this.falsePelanties = falsePelanties;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public int getYellowCard() {
        return yellowCard;
    }

    public void setYellowCard(int yellowCard) {
        this.yellowCard = yellowCard;
    }

    public int getRedCard() {
        return redCard;
    }

    public void setRedCard(int redCard) {
        this.redCard = redCard;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getTruePelanties() {
        return truePelanties;
    }

    public void setTruePelanties(int truePelanties) {
        this.truePelanties = truePelanties;
    }

    public int getFalsePelanties() {
        return falsePelanties;
    }

    public void setFalsePelanties(int falsePelanties) {
        this.falsePelanties = falsePelanties;
    }
}
