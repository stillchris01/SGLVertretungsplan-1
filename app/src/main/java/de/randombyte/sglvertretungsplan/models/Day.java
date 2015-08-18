package de.randombyte.sglvertretungsplan.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Day implements Parcelable { //Parcelable for transport from Activity to DayFragment

    private String timestamp;
    private String date;
    private String dayName;
    private String motd;
    private List<Vertretung> vertretungList = new ArrayList<>();

    public Day() {
    }

    private Day(Parcel source) {
        timestamp = source.readString();
        date = source.readString();
        dayName = source.readString();
        motd = source.readString();
        source.readList(vertretungList, Vertretung.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timestamp);
        dest.writeString(date);
        dest.writeString(dayName);
        dest.writeString(motd);
        dest.writeList(vertretungList);
    }
}
