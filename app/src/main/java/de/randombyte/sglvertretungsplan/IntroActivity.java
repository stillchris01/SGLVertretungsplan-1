package de.randombyte.sglvertretungsplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.google.inject.Inject;

import de.randombyte.sglvertretungsplan.events.LoginUpdatedEvent;
import de.randombyte.sglvertretungsplan.events.TestLoginFinishedEvent;
import de.randombyte.sglvertretungsplan.events.TestLoginStartEvent;
import de.randombyte.sglvertretungsplan.fragments.login.LoginIntroFragment;
import de.randombyte.sglvertretungsplan.selfmade.RoboIntro2;
import roboguice.RoboGuice;
import roboguice.event.EventManager;
import roboguice.event.Observes;

public class IntroActivity extends RoboIntro2 {

    private static final int NEXT_SLIDE_DELAY_AFTER_SUCCESSFULL_LOGIN = 1000;
    private @Inject EventManager eventManager;

    @Override
    public void init(Bundle bundle) {
        RoboGuice.getInjector(this).injectMembersWithoutViews(this); // look for @Oberves and register them

        LoginIntroFragment loginFragment = new LoginIntroFragment();
        RoboGuice.getInjector(this).injectMembersWithoutViews(loginFragment);

        addSlide(loginFragment);
    }

    public void onTestLoginFinished(@Observes TestLoginFinishedEvent event) {
        if (event.isSuccess()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isDestroyed()) {
                        Intent intent = new Intent(IntroActivity.this, EditProfileActivity.class);
                        intent.putExtra(EditProfileActivity.ARGS_IS_INTRO, true);
                        startActivity(intent);

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(IntroActivity.this).edit();
                        editor.putBoolean(MainApplication.PREFS_FIRST_START, false);
                        editor.apply();
                        finish();
                    }
                }
            }, NEXT_SLIDE_DELAY_AFTER_SUCCESSFULL_LOGIN);
        }
    }

    @Override
    public void onDonePressed() {
        eventManager.fire(new TestLoginStartEvent());
    }

    public void onLoginUpdated(@Observes LoginUpdatedEvent event) {
        LoginManager.save(PreferenceManager.getDefaultSharedPreferences(this), event.getCredentials());
    }
}
