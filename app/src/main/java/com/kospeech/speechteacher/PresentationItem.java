package com.kospeech.speechteacher;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PresentationItem implements Serializable {
    @SerializedName("presentation_id")
    private int presentation_id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("presentation_title")
    private String presntation_title;
    @SerializedName("presentation_time")
    private String presentation_time;
    @SerializedName("presentation_date")
    private String presentation_date;
    @SerializedName("presentation_ex_dupword")
    private String presentation_ex_dupword;
    @SerializedName("presentation_ex_improper")
    private String presentation_ex_improper;
    @SerializedName("presentation_result_count")
    private int presentation_result_count;
    @SerializedName("presentation_file_url")
    private String presentation_file_url;

    public int getPresentation_id() {
        return presentation_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPresntation_title() {
        return presntation_title;
    }

    public String getPresentation_time() {
        return presentation_time;
    }

    public String getPresentation_date() {
        return presentation_date;
    }

    public String getPresentation_ex_dupword() {
        return presentation_ex_dupword;
    }

    public String getPresentation_ex_improper() {
        return presentation_ex_improper;
    }

    public int getPresentation_result_count() {
        return presentation_result_count;
    }

    public String getPresentation_file_url() {
        return presentation_file_url;
    }

    @Override
    public String toString() {
        return "PresentationItem{" +
                "presentation_id=" + presentation_id +
                ", user_id='" + user_id + '\'' +
                ", presntation_title='" + presntation_title + '\'' +
                ", presentation_time='" + presentation_time + '\'' +
                ", presentation_date='" + presentation_date + '\'' +
                ", presentation_ex_dupword='" + presentation_ex_dupword + '\'' +
                ", presentation_ex_improper='" + presentation_ex_improper + '\'' +
                ", presentation_result_count=" + presentation_result_count +
                '}';
    }
}