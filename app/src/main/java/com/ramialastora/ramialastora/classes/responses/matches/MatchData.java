
package com.ramialastora.ramialastora.classes.responses.matches;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchData implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_team")
    @Expose
    private FirstSecondTeam firstTeam;
    @SerializedName("second_team")
    @Expose
    private FirstSecondTeam secondTeam;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("tour")
    @Expose
    private String tour;
    @SerializedName("live_video_url")
    @Expose
    private String liveVideoUrl;
    @SerializedName("match_video_url")
    @Expose
    private String matchVideoUrl;
    @SerializedName("league_active_id")
    @Expose
    private Integer leagueActiveId;
    @SerializedName("playground_id")
    @Expose
    private Integer playgroundId;
    @SerializedName("admin_id")
    @Expose
    private Integer adminId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("time_now")
    @Expose
    private String timeNow;
    @SerializedName("date_now")
    @Expose
    private String dateNow;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("elapsed_time")
    @Expose
    private String elapsedTime;
    @SerializedName("remaining_time")
    @Expose
    private String remainingTime;
    @SerializedName("playground")
    @Expose
    private Playground playground;
    @SerializedName("league_active")
    @Expose
    private LeagueActive leagueActive;
    @SerializedName("matchlive_video_url")
    @Expose
    private String matchliveVideoUrl;
    @SerializedName("sport_commentator")
    @Expose
    private SportCommentator sportCommentator;
    @SerializedName("sport_commentator1")
    @Expose
    private SportCommentator sportCommentator1;
    @SerializedName("get_channel")
    @Expose
    private GetChannel getChannel;
    @SerializedName("get_channel1")
    @Expose
    private GetChannel getChannel1;

    public MatchData() {
    }

    protected MatchData(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        startTime = in.readString();
        startDate = in.readString();
        result = in.readString();
        tour = in.readString();
        liveVideoUrl = in.readString();
        matchVideoUrl = in.readString();
        if (in.readByte() == 0) {
            leagueActiveId = null;
        } else {
            leagueActiveId = in.readInt();
        }
        if (in.readByte() == 0) {
            playgroundId = null;
        } else {
            playgroundId = in.readInt();
        }
        if (in.readByte() == 0) {
            adminId = null;
        } else {
            adminId = in.readInt();
        }
        createdAt = in.readString();
        updatedAt = in.readString();
        timeNow = in.readString();
        dateNow = in.readString();
        status = in.readInt();
        elapsedTime = in.readString();
        remainingTime = in.readString();
        matchliveVideoUrl = in.readString();
    }

    public static final Creator<MatchData> CREATOR = new Creator<MatchData>() {
        @Override
        public MatchData createFromParcel(Parcel in) {
            return new MatchData(in);
        }

        @Override
        public MatchData[] newArray(int size) {
            return new MatchData[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FirstSecondTeam getFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(FirstSecondTeam firstTeam) {
        this.firstTeam = firstTeam;
    }

    public FirstSecondTeam getSecondTeam() {
        return secondTeam;
    }

    public void setSecondTeam(FirstSecondTeam secondTeam) {
        this.secondTeam = secondTeam;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }

    public String getLiveVideoUrl() {
        return liveVideoUrl;
    }

    public void setLiveVideoUrl(String liveVideoUrl) {
        this.liveVideoUrl = liveVideoUrl;
    }

    public String getMatchVideoUrl() {
        return matchVideoUrl;
    }

    public void setMatchVideoUrl(String matchVideoUrl) {
        this.matchVideoUrl = matchVideoUrl;
    }

    public Integer getLeagueActiveId() {
        return leagueActiveId;
    }

    public void setLeagueActiveId(Integer leagueActiveId) {
        this.leagueActiveId = leagueActiveId;
    }

    public Integer getPlaygroundId() {
        return playgroundId;
    }

    public void setPlaygroundId(Integer playgroundId) {
        this.playgroundId = playgroundId;
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

    public String getTimeNow() {
        return timeNow;
    }

    public void setTimeNow(String timeNow) {
        this.timeNow = timeNow;
    }

    public String getDateNow() {
        return dateNow;
    }

    public void setDateNow(String dateNow) {
        this.dateNow = dateNow;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Playground getPlayground() {
        return playground;
    }

    public void setPlayground(Playground playground) {
        this.playground = playground;
    }

    public LeagueActive getLeagueActive() {
        return leagueActive;
    }

    public void setLeagueActive(LeagueActive leagueActive) {
        this.leagueActive = leagueActive;
    }

    public String getMatchliveVideoUrl() {
        return matchliveVideoUrl;
    }

    public void setMatchliveVideoUrl(String matchliveVideoUrl) {
        this.matchliveVideoUrl = matchliveVideoUrl;
    }

    public SportCommentator getSportCommentator() {
        return sportCommentator;
    }

    public void setSportCommentator(SportCommentator sportCommentator) {
        this.sportCommentator = sportCommentator;
    }

    public SportCommentator getSportCommentator1() {
        return sportCommentator1;
    }

    public void setSportCommentator1(SportCommentator sportCommentator1) {
        this.sportCommentator1 = sportCommentator1;
    }

    public GetChannel getGetChannel() {
        return getChannel;
    }

    public void setGetChannel(GetChannel getChannel) {
        this.getChannel = getChannel;
    }

    public GetChannel getGetChannel1() {
        return getChannel1;
    }

    public void setGetChannel1(GetChannel getChannel1) {
        this.getChannel1 = getChannel1;
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
        dest.writeString(startTime);
        dest.writeString(startDate);
        dest.writeString(result);
        dest.writeString(tour);
        dest.writeString(liveVideoUrl);
        dest.writeString(matchVideoUrl);
        if (leagueActiveId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(leagueActiveId);
        }
        if (playgroundId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(playgroundId);
        }
        if (adminId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(adminId);
        }
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(timeNow);
        dest.writeString(dateNow);
        dest.writeInt(status);
        dest.writeString(elapsedTime);
        dest.writeString(remainingTime);
        dest.writeString(matchliveVideoUrl);
    }
}
