package de.randombyte.sglvertretungsplan;

import android.content.SharedPreferences;
import com.google.gson.Gson;

import de.randombyte.sglvertretungsplan.models.Profile;
import de.randombyte.sglvertretungsplan.models.ProfileList;

public class ProfileManager {

    private static final String PREF_PROFLES = "pref_profiles";

    public static void save(SharedPreferences sharedPreferences, ProfileList profileList) {
        sharedPreferences
                .edit()
                .putString(PREF_PROFLES, new Gson().toJson(profileList))
                .apply();
    }

    public static ProfileList getAll(SharedPreferences sharedPreferences) {
        return new Gson().fromJson(sharedPreferences.getString(PREF_PROFLES, ""), ProfileList.class);
    }

    public static Profile getActive(SharedPreferences sharedPreferences) {
        ProfileList profileList = getAll(sharedPreferences);
        for (Profile profile : profileList.getProfileList()) {
            if (profile.getId() == profileList.getActiveProfileId()) {
                return profile;
            }
        }

        throw new IllegalStateException("No profile found with id "
                + profileList.getActiveProfileId() + " while searching for active profile!");
    }
}
