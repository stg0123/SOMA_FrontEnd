package com.kospeech.speechteacher;

import com.google.gson.annotations.SerializedName;

public class PresentationResultInfo {
    @SerializedName("presentation_result_id")
    private String presentation_result_id;
    @SerializedName("presentation_result")
    private PresentationResult presentation_result;
    @SerializedName("audiofile_url")
    private String audiofile_url;
    @SerializedName("presentation_result_time")
    private int presentation_result_time;
    @SerializedName("presentation_result_date")
    private String presentation_result_date;

    public int getPresentation_result_time() {
        return presentation_result_time;
    }

    public String getPresentation_result_id() {
        return presentation_result_id;
    }

    public PresentationResult getPresentation_result() {
        return presentation_result;
    }

    public String getAudiofile_url() {
        return audiofile_url;
    }

    public String getPresentation_result_date() {
        return presentation_result_date;
    }

    @Override
    public String toString() {
        return "PresentationResultInfo{" +
                "presentation_result_id='" + presentation_result_id + '\'' +
                ", presentation_result=" + presentation_result +
                ", audiofile_url='" + audiofile_url + '\'' +
                ", presentation_result_date='" + presentation_result_date + '\'' +
                '}';
    }
}
