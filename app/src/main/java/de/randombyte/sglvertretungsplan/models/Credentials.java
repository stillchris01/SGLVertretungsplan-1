package de.randombyte.sglvertretungsplan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Credentials implements Parcelable {

    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Credentials(Parcel source) {
        username = source.readString();
        password = source.readString();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel source) {
            return new Credentials(source);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
    }
}