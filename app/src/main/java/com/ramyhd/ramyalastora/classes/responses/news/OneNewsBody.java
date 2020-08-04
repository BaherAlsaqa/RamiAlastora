
package com.ramyhd.ramyalastora.classes.responses.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OneNewsBody {

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
    private NewsData data;

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

    public NewsData getData() {
        return data;
    }

    public void setData(NewsData data) {
        this.data = data;
    }

}
