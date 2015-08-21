package de.randombyte.sglvertretungsplan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Kurs implements Parcelable {

    private long creationTime;
    private boolean grundkurs;
    private int nummer;
    private String fach;

    public Kurs(long creationTime, boolean grundkurs, int nummer, String fach) {
        this.creationTime = creationTime;
        this.grundkurs = grundkurs;
        this.nummer = nummer;
        this.fach = fach;
    }

    private Kurs(Parcel source) {
        creationTime = source.readLong();
        grundkurs = (boolean) source.readValue(null);
        nummer = source.readInt();
        fach = source.readString();
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(int creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isGrundkurs() {
        return grundkurs;
    }

    public void setGrundkurs(boolean grundkurs) {
        this.grundkurs = grundkurs;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Kurs> CREATOR = new Creator<Kurs>() {
        @Override
        public Kurs createFromParcel(Parcel source) {
            return new Kurs(source);
        }

        @Override
        public Kurs[] newArray(int size) {
            return new Kurs[size];
        }
    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(creationTime);
        dest.writeValue(grundkurs);
        dest.writeInt(nummer);
        dest.writeString(fach);
    }

    @Override
    public String toString() {
        return (isGrundkurs() ? "GK" : "LK") + String.format("%02d", nummer) + "-" + fach; //GK04-S0
    }
}
