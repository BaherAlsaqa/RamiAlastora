package com.ramyhd.ramyalastora.classes.responses.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("en_name")
    @Expose
    private String en_name;
    @SerializedName("ar_name")
    @Expose
    private String ar_name;
    @SerializedName("en_nationality")
    @Expose
    private String en_nationality;
    @SerializedName("ar_nationality")
    @Expose
    private String ar_nationality;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getAr_name() {
        return ar_name;
    }

    public void setAr_name(String ar_name) {
        this.ar_name = ar_name;
    }

    public String getEn_nationality() {
        return en_nationality;
    }

    public void setEn_nationality(String en_nationality) {
        this.en_nationality = en_nationality;
    }

    public String getAr_nationality() {
        return ar_nationality;
    }

    public void setAr_nationality(String ar_nationality) {
        this.ar_nationality = ar_nationality;
    }
}
