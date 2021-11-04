package com.kospeech.speechteacher;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KnowhowItem implements Serializable {
    @SerializedName("knowhow_title")
    private String knowhow_title;
    @SerializedName("knowhow_img_url")
    private String knowhow_img_url;
    @SerializedName("knowhow_contents")
    private String knowhow_contents;

    public String getKnowhow_title() {
        return knowhow_title;
    }

    public String getKnowhow_img_url() {
        return knowhow_img_url;
    }

    public String getKnowhow_contents() {
        return knowhow_contents;
    }

    @Override
    public String toString() {
        return "KnowhowItem{" +
                "knowhow_title='" + knowhow_title + '\'' +
                ", knowhow_img_url='" + knowhow_img_url + '\'' +
                ", knowhow_contents='" + knowhow_contents + '\'' +
                '}';
    }
}