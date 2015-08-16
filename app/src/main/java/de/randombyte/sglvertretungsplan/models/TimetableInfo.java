package de.randombyte.sglvertretungsplan.models;

import com.google.gson.annotations.SerializedName;

public class TimetableInfo {
    @SerializedName("timetableurl") private String url;

    public String getUrl() {
        return url;
    }
}
