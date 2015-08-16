package de.randombyte.sglvertretungsplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import de.randombyte.sglvertretungsplan.events.VertretungsplanDownloadError;
import de.randombyte.sglvertretungsplan.events.VertretungsplanSaved;
import de.randombyte.sglvertretungsplan.models.Login;
import de.randombyte.sglvertretungsplan.models.Vertretungsplan;
import roboguice.event.EventManager;

public class VertretungsplanManager {

    private static final String PREF_VERTRETUNGSPLAN_KEY = "savedVertretungplan";

    public static void downloadAndSave(Context context, Login login, final EventManager eventManager) {

        new VertretungsplanDownloader(context, login) {

            @Override
            protected void onSuccess(Vertretungsplan vertretungsplan) throws Exception {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                String json = new Gson().toJson(vertretungsplan);
                editor.putString(PREF_VERTRETUNGSPLAN_KEY, json);
                editor.apply();
                eventManager.fire(new VertretungsplanSaved(vertretungsplan));
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                eventManager.fire(new VertretungsplanDownloadError(e));
            }
        }.execute();



    }

}
