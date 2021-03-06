package de.randombyte.sglvertretungsplan.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Kurs implements Parcelable, Comparable<Kurs> {

    private final long creationTime;
    private boolean grundkurs;
    private int nummer;
    private String fach;
    private String optionalLehrer;

    public Kurs(long creationTime, boolean grundkurs, int nummer, String fach, String optionalLehrer) {
        this.creationTime = creationTime;
        this.grundkurs = grundkurs;
        this.nummer = nummer;
        this.fach = fach;
        this.optionalLehrer = optionalLehrer;
    }

    private Kurs(Parcel source) {
        creationTime = source.readLong();
        grundkurs = (boolean) source.readValue(null);
        nummer = source.readInt();
        fach = source.readString();
        optionalLehrer = source.readString();
    }

    public long getCreationTime() {
        return creationTime;
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

    public String getOptionalLehrer() {
        return optionalLehrer;
    }

    public void setOptionalLehrer(String optionalLehrer) {
        this.optionalLehrer = optionalLehrer;
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
        dest.writeString(optionalLehrer);
    }

    @Override
    public String toString() {
        return (isGrundkurs() ? "GK" : "LK") + String.format("%02d", nummer) + "-" + fach; //GK04-S0
    }

    public String toStringDoppelblockung() {
        return toString() + (optionalLehrer != null && !optionalLehrer.isEmpty() ? "(" + optionalLehrer + ")" : ""); //GK04-S0(ROB)
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Kurs) {
            Kurs kurs = (Kurs) o;
            return creationTime == kurs.getCreationTime() &&
                    grundkurs == kurs.isGrundkurs() &&
                    nummer == kurs.getNummer() &&
                    fach.equalsIgnoreCase(kurs.getFach()) &&
                    (optionalLehrer == null ? kurs.getOptionalLehrer() == null :
                        optionalLehrer.equalsIgnoreCase(kurs.getOptionalLehrer())
                    );
        }

        return false;
    }

    /**
     * For ordering list of "Kurs"
     */
    @Override
    public int compareTo(@NonNull Kurs another) {
        return nummer - another.getNummer(); //GK2 is less than GK4: 2-4=-2
    }
}
