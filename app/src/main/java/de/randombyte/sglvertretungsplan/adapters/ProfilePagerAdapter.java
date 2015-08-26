package de.randombyte.sglvertretungsplan.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.randombyte.sglvertretungsplan.fragments.profile_fragments.ChooseKlasseFragment;
import de.randombyte.sglvertretungsplan.models.Profile;
import de.randombyte.sglvertretungsplan.fragments.profile_fragments.EditKursListFragment;

/**
 * Contains the fragment for selecting a "Stufe" or a list of "Kurs"
 */
public class ProfilePagerAdapter extends FragmentPagerAdapter {

    public static final int SCREENS_COUNT = 2;

    private final Profile profile;

    public ProfilePagerAdapter(FragmentManager fm, Profile profile) {
        super(fm);
        this.profile = profile;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ChooseKlasseFragment.newInstance(profile.getSuffix());

            case 1:
                return EditKursListFragment.newInstance(profile.getKursList());
        }

        throw new IllegalStateException("Position can't be " + position + "!"); //Shouldn't happen
    }

    @Override
    public int getCount() {
        return SCREENS_COUNT;
    }
}
