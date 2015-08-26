package de.randombyte.sglvertretungsplan;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import de.randombyte.sglvertretungsplan.models.Profile;

public class ProfileManager {

    private static final String PREF_PROFLE = "pref_profile";

    public static void save(SharedPreferences sharedPreferences, @NotNull Profile profile) {
        sharedPreferences
                .edit()
                .putString(PREF_PROFLE, new Gson().toJson(profile))
                .apply();
    }

    public static @Nullable Profile load(SharedPreferences sharedPreferences) {

        Profile profile = new Gson().fromJson(sharedPreferences.getString(PREF_PROFLE, ""), Profile.class);

        if (profile == null) {
            profile = new Profile();
            profile.setStufe("9");
            profile.setSuffix("a");
            profile.setOberstufe(false);
        }

        return profile;
    }
}
