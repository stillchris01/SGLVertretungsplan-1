package de.randombyte.sglvertretungsplan.models;

import java.util.ArrayList;
import java.util.List;

public class Day {

    private String timestamp;
    private String date;
    private String dayName;
    private String motd;
    private List<Vertretung> vertretungList = new ArrayList<>();

    public Day() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public List<Vertretung> getVertretungList() {
        return vertretungList;
    }

    public void setVertretungList(List<Vertretung> vertretungList) {
        this.vertretungList = vertretungList;
    }
}
