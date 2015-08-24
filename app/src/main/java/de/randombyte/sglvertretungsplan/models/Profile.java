package de.randombyte.sglvertretungsplan.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Profile implements Parcelable {

    private boolean oberstufe;
    private String stufe;
    private String suffix;
    private List<Kurs> kursList = new ArrayList<>();

    public Profile() {
    }

    private Profile(Parcel source) {
        oberstufe = (boolean) source.readValue(null);
        stufe = source.readString();
        suffix = source.readString();
        source.readList(kursList, Kurs.class.getClassLoader());
    }

    public boolean isOberstufe() {
        return oberstufe;
    }

    public void setOberstufe(boolean oberstufe) {
        this.oberstufe = oberstufe;
    }

    public String getStufe() {
        return stufe;
    }

    public void setStufe(String stufe) {
        this.stufe = stufe;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<Kurs> getKursList() {
        return kursList;
    }

    public void setKursList(List<Kurs> kursList) {
        this.kursList = kursList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(oberstufe);
        dest.writeString(stufe);
        dest.writeString(suffix);
        dest.writeList(kursList);
    }

    @Override
    public String toString() {
        return isOberstufe() ? stufe : stufe + suffix;
    }
}
