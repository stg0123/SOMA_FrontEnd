package com.kospeech.speechteacher;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class PresentationMakeItem implements Serializable {
    public String title;
    public ArrayList<ArrayList<String>> keywords;
    public ArrayList<String> script;
    public String time;
    public String date;

}
