
package com.ramyhd.ramialastora.classes.responses.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class NewsData implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("related_news")
    @Expose
    private ArrayList<RelatedNews> relatedNews = null;

    public NewsData() {
    }

    protected NewsData(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            teamId = null;
        } else {
            teamId = in.readInt();
        }
        title = in.readString();
        details = in.readString();
        image = in.readString();
        createdAt = in.readString();
        date = in.readString();
    }

    public static final Creator<NewsData> CREATOR = new Creator<NewsData>() {
        @Override
        public NewsData createFromParcel(Parcel in) {
            return new NewsData(in);
        }

        @Override
        public NewsData[] newArray(int size) {
            return new NewsData[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<RelatedNews> getRelatedNews() {
        return relatedNews;
    }

    public void setRelatedNews(ArrayList<RelatedNews> relatedNews) {
        this.relatedNews = relatedNews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (teamId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(teamId);
        }
        dest.writeString(title);
        dest.writeString(details);
        dest.writeString(image);
        dest.writeString(createdAt);
        dest.writeString(date);
    }
}
