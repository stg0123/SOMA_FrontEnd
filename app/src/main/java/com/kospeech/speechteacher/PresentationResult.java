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

    public Map<String, Integer> getDuplicatedWords() {
        return duplicatedWords;
    }

    public Map<String, List<String>> getUnsuitableWords() {
        return unsuitableWords;
    }

    public List<List<Float>> getGap() {
        return gap;
    }

    public List<Float> getTune() {
        return tune;
    }

    public List<List<Float>> getSpeed() {
        return speed;
    }

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