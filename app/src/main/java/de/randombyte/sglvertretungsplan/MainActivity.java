package de.randombyte.sglvertretungsplan;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.Arrays;
import java.util.Calendar;

import de.randombyte.sglvertretungsplan.adapters.DayPagerAdapter;
import de.randombyte.sglvertretungsplan.events.TestButtonClicked;
import de.randombyte.sglvertretungsplan.events.VertretungsplanDownloadError;
import de.randombyte.sglvertretungsplan.events.VertretungsplanSaved;
import de.randombyte.sglvertretungsplan.models.Kurs;
import de.randombyte.sglvertretungsplan.models.Login;
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

    private @InjectView(R.id.testing_button) Button button;
    private @InjectView(R.id.test_textview) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting test Profile
        Profile testProfile = new Profile();
        testProfile.setOberstufe(true);
        testProfile.setStufe("EF");
        //testProfile.setSuffix("d"); //or c
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        Kurs music = new Kurs(timeInMillis, true, 6, "MU");
        Kurs de = new Kurs(timeInMillis + 1, true, 7 ,"D");
        testProfile.setKursList(Arrays.asList(music, de));
        ProfileManager.save(PreferenceManager.getDefaultSharedPreferences(this), testProfile);

        TypedArray accentColor = obtainStyledAttributes(new TypedValue().data, new int[]{R.attr.colorAccent});
        tabLayout.setTabTextColors(0xB3FFFFFF, accentColor.getColor(0, Color.WHITE)); //70% white
        accentColor.recycle();

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        VertretungsplanManager.downloadAndSave(this, new Login("bla", "blub"), eventManager);
    }

    public void onTestButtonClick(@Observes TestButtonClicked clicked) {
        VertretungsplanManager.downloadAndSave(this, new Login("bla", "blub"), eventManager);
        textView.setText("Loading");
    }

    public void onVertretungsplanSaved(@Observes VertretungsplanSaved event) {

        showVertretungsplan(event.getVertretungsplan());

        /*String text = "";
        for (Day day : event.getVertretungsplan().getDays()) {
            text += day.getDayName() + ":\n";
            for (Vertretung vertretung : day.getVertretungList()) {
                text += vertretung.getKlasse() + ", ";
            }
        }
        textView.setText(text);*/
    }

    public void showVertretungsplan(Vertretungsplan vertretungsplan) {
        if (vertretungsplan == null) {
            throw new IllegalArgumentException("Vertretungsplan must not be null");
        }

        Profile profile = ProfileManager
                .load(PreferenceManager.getDefaultSharedPreferences(this));

        PagerAdapter pagerAdapter = viewPager.getAdapter();
        DayPagerAdapter dayPagerAdapter; //todo: crappy?
        if (pagerAdapter == null) {
            dayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager());
        } else {
            dayPagerAdapter = (DayPagerAdapter) pagerAdapter;
        }
        dayPagerAdapter.setProfile(profile);
        dayPagerAdapter.setVertretungsplan(vertretungsplan);

        viewPager.setAdapter(dayPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setTitle("Vertretungsplan - " + profile.toString());
    }

    public void onVertretungsplanDownloadError(@Observes VertretungsplanDownloadError event) {
        textView.setText("Error:\n" + event.getException().getMessage());
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
                startActivity(new Intent(this, EditProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
