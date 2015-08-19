package de.randombyte.sglvertretungsplan;

import android.content.SharedPreferences;
import com.google.gson.Gson;

import de.randombyte.sglvertretungsplan.models.Profile;

public class ProfileManager {

    private static final String PREF_PROFLE = "pref_profile";

    public static void save(SharedPreferences sharedPreferences, Profile profile) {
        sharedPreferences
                .edit()
                .putString(PREF_PROFLE, new Gson().toJson(profile))
                .apply();
    }

    public static Profile load(SharedPreferences sharedPreferences) {
        return new Gson().fromJson(sharedPreferences.getString(PREF_PROFLE, ""), Profile.class);
    }
}
