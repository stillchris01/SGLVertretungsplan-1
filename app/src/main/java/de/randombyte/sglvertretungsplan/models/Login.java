package de.randombyte.sglvertretungsplan.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.WorkerThread;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Login implements Parcelable {

    private static final String AUTH_ID_URL = "http://iphone.dsbcontrol.de/iPhoneService.svc/DSB/authid/";
    private static final String TIMETABLES_URL = "http://iphone.dsbcontrol.de/iPhoneService.svc/DSB/timetables/";

    private String username;
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Login(Parcel source) {
        username = source.readString();
        password = source.readString();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @WorkerThread
    public TimetableInfo[] loadLinks() throws IOException {

        TimetableInfo[] timetableInfo;

        Scanner authIdScanner = null;
        Scanner timetableInfoScanner = null;

        URL authIdUrl = new URL(AUTH_ID_URL + username + "/" + password);
        try {
            authIdScanner = new Scanner(authIdUrl.openStream());
            String authId = authIdScanner.nextLine().replace("\"", "");

            timetableInfoScanner = new Scanner(new URL(TIMETABLES_URL + authId).openStream());
            String response = timetableInfoScanner.nextLine();

            timetableInfo = new Gson().fromJson(response, TimetableInfo[].class);
        } finally {
            if (authIdScanner != null) {
                authIdScanner.close();
            }
            if (timetableInfoScanner != null) {
                timetableInfoScanner.close();
            }
        }

        return timetableInfo;
    }

    public static boolean isBadLink(String url) {
        return url.contains("NoContent");
    }

    public static final Creator<Login> CREATOR = new Creator<Login>() {
        @Override
        public Login createFromParcel(Parcel source) {
            return new Login(source);
        }

        @Override
        public Login[] newArray(int size) {
            return new Login[size];
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