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
    private List<List<Integer>> gap;
    @SerializedName("tune")
    private List<Integer> tune;
    @SerializedName("speed")
    private List<List<Integer>> speed;
    @SerializedName("fillerWords")
    private Map<String,List<Integer>> fillerWords;

    public Map<String, List<Integer>> getFillerWords() {
        return fillerWords;
    }

    public Map<String, Integer> getDuplicatedWords() {
        return duplicatedWords;
    }

    public Map<String, List<String>> getUnsuitableWords() {
        return unsuitableWords;
    }

    public List<List<Integer>> getGap() {
        return gap;
    }

    public List<Integer> getTune() {
        return tune;
    }

    public List<List<Integer>> getSpeed() {
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