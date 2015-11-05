package de.randombyte.sglvertretungsplan;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import de.randombyte.sglvertretungsplan.fragments.SettingsFragment;
import de.randombyte.sglvertretungsplan.models.Credentials;

public class LoginManager {

    public static void save(SharedPreferences sharedPreferences, @NotNull Credentials credentials) {
        sharedPreferences
                .edit()
                .putString(SettingsFragment.PREFS_LOGIN, new Gson().toJson(credentials))
                .apply();
    }

    public static @Nullable
    Credentials load(SharedPreferences sharedPreferences) {
        return new Gson().fromJson(sharedPreferences.getString(SettingsFragment.PREFS_LOGIN, ""), Credentials.class);
    }

}
