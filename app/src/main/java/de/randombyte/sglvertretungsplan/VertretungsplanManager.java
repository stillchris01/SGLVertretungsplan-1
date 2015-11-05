package de.randombyte.sglvertretungsplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import de.randombyte.sglvertretungsplan.events.VertretungsplanDownloadError;
import de.randombyte.sglvertretungsplan.events.VertretungsplanDownloadStartedEvent;
import de.randombyte.sglvertretungsplan.events.VertretungsplanSavedEvent;
import de.randombyte.sglvertretungsplan.models.Credentials;
import de.randombyte.sglvertretungsplan.models.VertretungsplanAndLogin;
import roboguice.event.EventManager;

public class VertretungsplanManager {

    private static final String PREF_VERTRETUNGSPLAN_KEY = "savedVertretungplan";

    public static void downloadAndSave(Context context, Credentials credentials, final EventManager eventManager) {

        new VertretungsplanDownloader(context, credentials) {

            @Override
            protected void onPreExecute() throws Exception {
                super.onPreExecute();

                eventManager.fire(new VertretungsplanDownloadStartedEvent());
            }

            @Override
            protected void onSuccess(VertretungsplanAndLogin vertretungsplanAndLogin) throws Exception {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREF_VERTRETUNGSPLAN_KEY,
                        new Gson().toJson(vertretungsplanAndLogin.getVertretungsplan()));
                editor.apply();

                // Saving lastAuthId
                LoginManager.save(sharedPreferences, vertretungsplanAndLogin.getCredentials());

                eventManager.fire(new VertretungsplanSavedEvent(vertretungsplanAndLogin.getVertretungsplan()));
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                eventManager.fire(new VertretungsplanDownloadError(e));
            }
        }.execute();
    }
}
