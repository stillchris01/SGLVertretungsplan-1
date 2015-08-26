package de.randombyte.sglvertretungsplan;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import de.randombyte.sglvertretungsplan.fragments.SettingsFragment;
import roboguice.RoboGuice;
import roboguice.activity.RoboActionBarActivity;

public class SettingsActivity extends RoboActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SettingsFragment settingsFragment = new SettingsFragment();
        RoboGuice.getInjector(this).injectMembersWithoutViews(settingsFragment);

        getFragmentManager().beginTransaction()
                .replace(R.id.main_content, settingsFragment)
                .commit();
    }
}
