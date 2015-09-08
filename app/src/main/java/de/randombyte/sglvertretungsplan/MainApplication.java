package de.randombyte.sglvertretungsplan;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import roboguice.activity.RoboActivity;

/**
 * For making the decision if the intro screen should be shown
 */
public class MainApplication extends RoboActivity {

    public static final String PREFS_FIRST_START = "prefs_first_start";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PREFS_FIRST_START, true)) {
            startActivity(new Intent(this, IntroActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }
}
