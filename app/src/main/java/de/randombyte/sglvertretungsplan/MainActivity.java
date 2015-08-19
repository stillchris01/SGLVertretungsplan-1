package de.randombyte.sglvertretungsplan;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.Arrays;

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

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.tab_layout) TabLayout tabLayout;
    @InjectView(R.id.view_pager) ViewPager viewPager;

    @InjectView(R.id.testing_button) Button button;
    @InjectView(R.id.test_textview) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting test Profile
        Profile testProfile = new Profile();
        testProfile.setOberstufe(true);
        testProfile.setStufe("EF");
        //testProfile.setSuffix("d"); //or c
        Kurs music = new Kurs();
        music.setGrundkurs(true);
        music.setNummer(6);
        music.setFach("MU");
        Kurs de = new Kurs();
        de.setGrundkurs(true);
        de.setNummer(7);
        de.setFach("D");
        testProfile.setKursList(Arrays.asList(music, de));
        ProfileManager.save(PreferenceManager.getDefaultSharedPreferences(this), testProfile);

        toolbar.setTitle("Testing mode");
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
    }

    public void onVertretungsplanDownloadError(@Observes VertretungsplanDownloadError event) {
        textView.setText("Error:\n" + event.getException().getMessage());
    }
}
