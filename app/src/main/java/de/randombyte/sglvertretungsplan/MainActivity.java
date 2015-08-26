package de.randombyte.sglvertretungsplan;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.inject.Inject;

import org.jetbrains.annotations.NotNull;

import de.randombyte.sglvertretungsplan.adapters.DayPagerAdapter;
import de.randombyte.sglvertretungsplan.events.VertretungsplanDownloadError;
import de.randombyte.sglvertretungsplan.events.VertretungsplanSaved;
import de.randombyte.sglvertretungsplan.models.Profile;
import de.randombyte.sglvertretungsplan.models.Vertretungsplan;
import roboguice.activity.RoboActionBarActivity;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity {

    @Inject EventManager eventManager;

    private @InjectView(R.id.toolbar) Toolbar toolbar;
    private @InjectView(R.id.tab_layout) TabLayout tabLayout;
    private @InjectView(R.id.view_pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypedArray accentColor = obtainStyledAttributes(new TypedValue().data, new int[]{R.attr.colorAccent});
        tabLayout.setTabTextColors(0xB3FFFFFF, accentColor.getColor(0, Color.WHITE)); //70% white
        accentColor.recycle();

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        VertretungsplanManager.downloadAndSave(this,
                LoginManager.load(PreferenceManager.getDefaultSharedPreferences(this)), eventManager);
    }

    public void onVertretungsplanSaved(@Observes VertretungsplanSaved event) {
        showVertretungsplan(event.getVertretungsplan());
    }

    public void showVertretungsplan(@NotNull Vertretungsplan vertretungsplan) {

        Profile profile = ProfileManager
                .load(PreferenceManager.getDefaultSharedPreferences(this));
        if (profile == null) {
            //todo: should never happen
            new AlertDialog.Builder(this).setMessage("Profil in den Einstellungen erstellen!").create().show();
            return;
        }

        PagerAdapter pagerAdapter = viewPager.getAdapter();
        DayPagerAdapter dayPagerAdapter;
        if (pagerAdapter == null) {
            dayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager());
        } else {
            dayPagerAdapter = (DayPagerAdapter) pagerAdapter;
        }
        dayPagerAdapter.setProfile(profile);
        dayPagerAdapter.setVertretungsplan(vertretungsplan);

        viewPager.setAdapter(dayPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        hideVertretungsplanViews(false);

        toolbar.setTitle("Vertretungsplan - " + profile.toString());
    }

    public void onVertretungsplanDownloadError(@Observes VertretungsplanDownloadError event) {
        hideVertretungsplanViews(true);
        new AlertDialog.Builder(this).setMessage("Server akzeptiert Logindaten nicht!").create().show();
    }

    private void hideVertretungsplanViews(boolean hide) {
        viewPager.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        tabLayout.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_announcments:
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
