package com.ramyhd.ramialastora.classes.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyData {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private DataPaginate dataPaginate;

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public DataPaginate getDataPaginate() {
        return dataPaginate;
    }

    public void setDataPaginate(DataPaginate dataPaginate) {
        this.dataPaginate = dataPaginate;
    }
}
