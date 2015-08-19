package de.randombyte.sglvertretungsplan.models;

import android.support.annotation.WorkerThread;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Login {

    private static final String AUTH_ID_URL = "http://iphone.dsbcontrol.de/iPhoneService.svc/DSB/authid/";
    private static final String TIMETABLES_URL = "http://iphone.dsbcontrol.de/iPhoneService.svc/DSB/timetables/";

    private final String username = "1"; //Only for testing todo
    private final String password = "1";

    public Login(String username, String password) {
        //this.username = username;
        //this.password = password;
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
}