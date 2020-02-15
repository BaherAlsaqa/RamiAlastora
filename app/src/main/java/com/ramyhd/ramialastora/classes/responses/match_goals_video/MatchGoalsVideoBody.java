
package com.ramyhd.ramialastora.classes.responses.match_goals_video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MatchGoalsVideoBody {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private ArrayList<MatchGoalsVideoData> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<MatchGoalsVideoData> getData() {
        return data;
    }

    public void setData(ArrayList<MatchGoalsVideoData> data) {
        this.data = data;
    }

}
