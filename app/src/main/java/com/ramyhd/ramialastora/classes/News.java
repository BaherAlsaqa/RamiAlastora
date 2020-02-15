package com.ramyhd.ramialastora.classes;

public class News {
    public int image;
    public String time;
    public String title;

    public News(int image, String time, String title) {
        this.image = image;
        this.time = time;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
