package de.randombyte.sglvertretungsplan.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import de.randombyte.sglvertretungsplan.DayFragment;
import de.randombyte.sglvertretungsplan.models.Profile;
import de.randombyte.sglvertretungsplan.models.Vertretungsplan;

/**
 * For ViewPager, using FragmentStatePagerAdapter so it destroys the fragments when notifyDataSetChanged() is called
 */
public class DayPagerAdapter extends FragmentStatePagerAdapter {

    private Vertretungsplan vertretungsplan;
    private Profile profile;

    public DayPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setVertretungsplan(@NotNull Vertretungsplan vertretungsplan) {
        this.vertretungsplan = vertretungsplan;
    }

    public void setProfile(@NotNull Profile profile) {
        this.profile = profile;
    }

    @Override
    public Fragment getItem(int position) {
        return DayFragment.newInstance(vertretungsplan.getDays().get(position), profile);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE; //To force recreation with setAdapter() or notifyDataSetChanged()
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
