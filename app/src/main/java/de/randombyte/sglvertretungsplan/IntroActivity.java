package de.randombyte.sglvertretungsplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import de.randombyte.sglvertretungsplan.events.LoginUpdatedEvent;
import de.randombyte.sglvertretungsplan.fragments.login.LoginIntroFragment;
import de.randombyte.sglvertretungsplan.selfmade.RoboIntro2;
import roboguice.RoboGuice;
import roboguice.event.Observes;

public class IntroActivity extends RoboIntro2 {

    @Override
    public void init(Bundle bundle) {
        LoginIntroFragment loginFragment = new LoginIntroFragment();
        RoboGuice.getInjector(this).injectMembersWithoutViews(loginFragment);

        addSlide(loginFragment);
    }

    @Override
    public void onDonePressed() {
        startActivity(new Intent(this, MainActivity.class));
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean(MainApplication.PREFS_FIRST_START, false);
        editor.apply();
        finish();
    }

    public void onLoginUpdated(@Observes LoginUpdatedEvent event) {
        LoginManager.save(PreferenceManager.getDefaultSharedPreferences(this), event.getLogin());
    }
}
