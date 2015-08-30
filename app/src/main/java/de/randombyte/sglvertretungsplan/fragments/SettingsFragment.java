package de.randombyte.sglvertretungsplan.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import de.randombyte.sglvertretungsplan.LoginManager;
import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.events.LoginUpdatedEvent;
import de.randombyte.sglvertretungsplan.fragments.login.LoginDialog;
import roboguice.RoboGuice;
import roboguice.event.Observes;

public class SettingsFragment extends PreferenceFragment {

    public static final String PREFS_LOGIN = "prefs_login";
    private static final int DIALOG_CLOSE_DELAY_AFTER_SUCCESSFULL_LOGIN = 1500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        findPreference(PREFS_LOGIN).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                //LoginFragment in dialog
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = fragmentManager.findFragmentByTag(LoginDialog.TAG);
                if (fragment != null) {
                    fragmentTransaction.remove(fragment);
                }
                fragmentTransaction.addToBackStack(null);

                LoginDialog dialog = LoginDialog.newInstance(LoginManager.load(
                        PreferenceManager.getDefaultSharedPreferences(getActivity())
                ));
                RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(dialog);
                dialog.show(fragmentTransaction, LoginDialog.TAG);

                return true;
            }
        });
    }

/*    public void onTestLoginFinished(@Observes TestLoginFinishedEvent event) {

        if (event.isSuccess()) {
            Handler handler = new Handler();
            final Runnable closeDialog = new Runnable() {
                @Override
                public void run() {
                    if (SettingsFragment.this.getActivity() != null) {
                        //Only do in attached state, user could close settings manually in the delay time
                        FragmentManager fragmentManager = SettingsFragment.this.getFragmentManager();
                        Fragment loginFragment = fragmentManager.findFragmentByTag(LoginFragment.TAG);
                        if (loginFragment != null) {
                            //Check if dialog was closed manually
                            fragmentManager.beginTransaction().remove(loginFragment).commit();
                        }
                    }
                }
            };
            handler.postDelayed(closeDialog, DIALOG_CLOSE_DELAY_AFTER_SUCCESSFULL_LOGIN);
        }
    }*/

    public void onLoginUpdated(@Observes LoginUpdatedEvent event) {
        LoginManager.save(PreferenceManager.getDefaultSharedPreferences(getActivity()), event.getLogin());
    }
}
