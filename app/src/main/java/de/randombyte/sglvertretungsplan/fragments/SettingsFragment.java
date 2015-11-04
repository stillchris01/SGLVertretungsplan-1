package de.randombyte.sglvertretungsplan.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import de.randombyte.sglvertretungsplan.LoginManager;
import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.events.LoginUpdatedEvent;
import de.randombyte.sglvertretungsplan.fragments.login.LoginDialog;
import de.randombyte.sglvertretungsplan.models.Login;
import roboguice.RoboGuice;
import roboguice.event.Observes;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String PREFS_LOGIN = "prefs_login";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        findPreference(PREFS_LOGIN).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showLoginDialog(getActivity(), getChildFragmentManager(), LoginManager.load(
                        PreferenceManager.getDefaultSharedPreferences(getActivity())
                ));

                return true;
            }
        });

        findPreference("prefs_debug_link").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Login login = LoginManager.load(PreferenceManager.getDefaultSharedPreferences(getActivity()));
                if (login.getLastAuthId() == null) {
                    Toast.makeText(getActivity(), "Bitte Vertretungsplan zuerst neu laden!",
                            Toast.LENGTH_LONG).show();

                    return true;
                }

                Intent debugLinkShowIntent = new Intent();
                debugLinkShowIntent.setData(Uri.parse(Login.TIMETABLES_URL + "/" + login.getLastAuthId()));
                startActivity(debugLinkShowIntent);

                return true;
            }
        });
    }

    /**
     * Shows the LoginDialog
     * @param context Context to be used for RoboGuice EventManger
     * @param fragmentManager FragmentManager where the dialog will be shown
     * @param login Login which is shown in the dialog
     * @return The shown LoginDialog
     */
    public static LoginDialog showLoginDialog(Context context, FragmentManager fragmentManager, Login login) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(LoginDialog.TAG);
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.addToBackStack(null);

        LoginDialog dialog = LoginDialog.newInstance(login);
        RoboGuice.getInjector(context).injectMembersWithoutViews(dialog);
        dialog.show(fragmentTransaction, LoginDialog.TAG);

        return dialog;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_about) {
            new LibsBuilder()
                    .withFields(R.string.class.getFields())
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withLibraries("Jsoup", "AppCompat Library", "Support Library")
                    .withAboutIconShown(true)
                    .withAboutVersionShown(true)
                    .withActivityTitle("Credits")
                    .start(getActivity());
        }

        return super.onOptionsItemSelected(item);
    }
}
