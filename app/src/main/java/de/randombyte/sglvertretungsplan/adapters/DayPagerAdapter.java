package de.randombyte.sglvertretungsplan.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.randombyte.sglvertretungsplan.DayFragment;
import de.randombyte.sglvertretungsplan.models.Profile;
import de.randombyte.sglvertretungsplan.models.Vertretungsplan;

/**
 * For ViewPager
 */
public class DayPagerAdapter extends FragmentPagerAdapter {

    private Vertretungsplan vertretungsplan;
    private Profile profile;

    public DayPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setVertretungsplan(Vertretungsplan vertretungsplan) {
        this.vertretungsplan = vertretungsplan;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public Fragment getItem(int position) {
        return DayFragment.newInstance(vertretungsplan.getDays().get(position), profile);
    }

    @Override
    public int getCount() {
        return vertretungsplan.getDays().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return vertretungsplan.getDays().get(position).getDayName();
    }
}
