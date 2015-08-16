package de.randombyte.sglvertretungsplan.models;

import java.util.ArrayList;
import java.util.List;

public class Vertretungsplan {

    private long saveTime;
    private List<Day> days = new ArrayList<>();

    public Vertretungsplan() {
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
