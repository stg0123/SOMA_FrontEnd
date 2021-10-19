package com.kospeech.speechteacher;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PresentationResult implements Serializable {
    @SerializedName("duplicatedWords")
    private Map<String,Integer> duplicatedWords;
    @SerializedName("unsuitableWords")
    private Map<String, List<String>> unsuitableWords;
    @SerializedName("gap")
    private List<List<Float>> gap;
    @SerializedName("tune")
    private List<Float> tune;
    @SerializedName("speed")
    private List<List<Float>> speed;


    @Override
    public String toString() {
        return "PresentationResult{" +
                "duplicatedWords=" + duplicatedWords +
                ", unsuitableWords=" + unsuitableWords +
                ", gap=" + gap +
                ", tune=" + tune +
                ", speed=" + speed +
                '}';
    }
}