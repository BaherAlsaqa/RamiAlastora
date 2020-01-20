package com.ramialastora.ramialastora.classes;

public class Notification {
    public String title;
    public String time;
    public int type;

    public Notification(String title, String time, int type) {
        this.title = title;
        this.time = time;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
