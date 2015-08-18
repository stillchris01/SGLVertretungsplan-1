package de.randombyte.sglvertretungsplan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Kurs implements Parcelable {

    private int id;
    private boolean grundkurs;
    private int nummer;
    private String fach;

    public Kurs() {
    }

    private Kurs(Parcel source) {
        id = source.readInt();
        grundkurs = (boolean) source.readValue(null);
        nummer = source.readInt();
        fach = source.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        dest.writeInt(id);
        dest.writeValue(grundkurs);
        dest.writeInt(nummer);
        dest.writeString(fach);
    }

    @Override
    public String toString() {
        return isGrundkurs() ? "GK" : "LK" + String.format("%02d", nummer) + "-" + fach; //GK04-S0
    }
}
