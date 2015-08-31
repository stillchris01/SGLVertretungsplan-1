package de.randombyte.sglvertretungsplan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TimetableInfo implements Parcelable {
    @SerializedName("timetableurl") private String url;

    private TimetableInfo(Parcel in) {
        url = in.readString();
    }

    public String getUrl() {
        return url;
    }

    public static final Creator<TimetableInfo> CREATOR = new Creator<TimetableInfo>() {
        @Override
        public TimetableInfo createFromParcel(Parcel in) {
            return new TimetableInfo(in);
        }

        @Override
        public TimetableInfo[] newArray(int size) {
            return new TimetableInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }
}
