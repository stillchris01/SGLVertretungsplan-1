package de.randombyte.sglvertretungsplan;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import de.randombyte.sglvertretungsplan.fragments.SettingsFragment;
import de.randombyte.sglvertretungsplan.models.Login;

public class LoginManager {

    public static void save(SharedPreferences sharedPreferences, @NotNull Login login) {
        sharedPreferences
                .edit()
                .putString(SettingsFragment.PREFS_LOGIN, new Gson().toJson(login))
                .apply();
    }

    public static @Nullable Login load(SharedPreferences sharedPreferences) {
        return new Gson().fromJson(sharedPreferences.getString(SettingsFragment.PREFS_LOGIN, ""), Login.class);
    }

}
