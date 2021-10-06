package com.kospeech.speechteacher;

import com.google.gson.annotations.SerializedName;

public class ErrorData {
    @SerializedName("message")
    public String message;

    @Override
    public String toString() {
        return "ErrorData{" +
                "message='" + message + '\'' +
                '}';
    }
}
