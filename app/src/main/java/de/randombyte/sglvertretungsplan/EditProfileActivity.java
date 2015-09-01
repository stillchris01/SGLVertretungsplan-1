package de.randombyte.sglvertretungsplan;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Arrays;

import de.randombyte.sglvertretungsplan.adapters.ProfilePagerAdapter;
import de.randombyte.sglvertretungsplan.customviews.NonSwipeableViewPager;
import de.randombyte.sglvertretungsplan.events.KlasseChooseEvent;
import de.randombyte.sglvertretungsplan.events.KursListUpdatedEvent;
import de.randombyte.sglvertretungsplan.models.Profile;
import roboguice.activity.RoboActionBarActivity;
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_edit_profile)
public class EditProfileActivity extends RoboActionBarActivity {

    private  @InjectView(R.id.toolbar) Toolbar toolbar;
    private  @InjectView(R.id.spinner_stufe) Spinner spinnerStufe;
    private  @InjectView(R.id.view_pager) NonSwipeableViewPager viewPager;

    private Profile profile;
    private String[] stufeList;

    @Override
    protected void onResume() {
        super.onResume();

        profile = ProfileManager.load(PreferenceManager.getDefaultSharedPreferences(this)); //todo: duplicate
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stufeList = getResources().getStringArray(R.array.spinner_stufe_entries);

        profile = ProfileManager.load(PreferenceManager.getDefaultSharedPreferences(this));

        viewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager(), profile));

        spinnerStufe.setSelection(stufeToSpinnerPos(this, profile.getStufe()));
        spinnerStufe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profile.setStufe(stufeList[position]);
                // Oberstufe is not numeric(EF, Q1, Q2)
                boolean oberstufe = !profile.getStufe().matches("\\d+");
                profile.setOberstufe(oberstufe);
                viewPager.setCurrentItem(oberstufe ? 1 : 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onKlasseChosen(@Observes KlasseChooseEvent event) {
        profile.setSuffix(event.getKlasse());
    }

    public void onKursListUpdated(@Observes KursListUpdatedEvent event) {
        profile.setKursList(event.getKursList());
    }

    @Override
    protected void onPause() {
        super.onPause();

        ProfileManager.save(PreferenceManager.getDefaultSharedPreferences(this), profile);
    }

    private static int stufeToSpinnerPos(Context context, String stufe) {
        return Arrays.asList(context.getResources().getStringArray(R.array.spinner_stufe_entries))
                .indexOf(stufe);
    }
}
