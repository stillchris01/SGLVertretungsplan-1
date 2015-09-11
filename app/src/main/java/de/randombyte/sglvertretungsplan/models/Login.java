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
    public static final String INVALID_AUTH_ID = "00000000-0000-0000-0000-000000000000";

    private String username;
    private String password;
    private String lastAuthId;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Login(Parcel source) {
        username = source.readString();
        password = source.readString();
        lastAuthId = source.readString();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLastAuthId() {
        return lastAuthId;
    }

    public void setLastAuthId(String lastAuthId) {
        this.lastAuthId = lastAuthId;
    }

    @WorkerThread
    public TimetableInfo[] loadLinks() throws IOException {

        TimetableInfo[] timetableInfo;

        // First run, trying lastAuthId
        if (lastAuthId != null && !lastAuthId.isEmpty() && !lastAuthId.equals(INVALID_AUTH_ID)) {
            // Can be valid
            timetableInfo = loadTimetableInfo(lastAuthId);
            if (isBadLink(timetableInfo[0].getUrl())) {
                // Invalid authId -> removing it do it will be reloaded
                lastAuthId = null;
                return loadLinks();
            } else {
                // Success with lastAuthId
                return timetableInfo;
            }
        } else {
            // No lastAuthId there
            lastAuthId = loadAuthId(username, password);
            return loadLinks();
        }
    }

    @WorkerThread
    private static String loadAuthId(String username, String password) throws IOException {
        Scanner authIdScanner = null;
        String authId;
        try {
            URL authIdUrl = new URL(AUTH_ID_URL + username + "/" + password);
            authIdScanner = new Scanner(authIdUrl.openStream());
            authId = authIdScanner.nextLine().replace("\"", "");
        } finally {
            if (authIdScanner != null) {
                authIdScanner.close();
            }
        }

        return authId;
    }

    @WorkerThread
    private static TimetableInfo[] loadTimetableInfo(String authId) throws IOException {
        TimetableInfo[] timetableInfo;
        Scanner timetableInfoScanner = null;

        try {
            timetableInfoScanner = new Scanner(new URL(TIMETABLES_URL + authId).openStream());
            String response = timetableInfoScanner.nextLine();

            timetableInfo = new Gson().fromJson(response, TimetableInfo[].class);
        } finally {
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
        dest.writeString(lastAuthId);
    }
}