package de.randombyte.sglvertretungsplan;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.inject.Inject;

import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

import de.randombyte.sglvertretungsplan.adapters.DayPagerAdapter;
import de.randombyte.sglvertretungsplan.events.LoginUpdatedEvent;
import de.randombyte.sglvertretungsplan.events.VertretungsplanDownloadError;
import de.randombyte.sglvertretungsplan.events.VertretungsplanDownloadStartedEvent;
import de.randombyte.sglvertretungsplan.events.VertretungsplanSavedEvent;
import de.randombyte.sglvertretungsplan.fragments.SettingsFragment;
import de.randombyte.sglvertretungsplan.models.Profile;
import de.randombyte.sglvertretungsplan.models.Vertretungsplan;
import roboguice.activity.RoboActionBarActivity;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import tr.xip.errorview.ErrorView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity {

    private ErrorView.Config loginErrorConfig;
    private ErrorView.Config internetErrorConfig;

    private @Inject EventManager eventManager;

    private @InjectView(R.id.root_view) View rootView;
    private @InjectView(R.id.toolbar) Toolbar toolbar;
    private @InjectView(R.id.tab_layout) TabLayout tabLayout;
    private @InjectView(R.id.view_pager) ViewPager viewPager;
    private @InjectView(R.id.progress) ProgressBar progressBar;
    private @InjectView(R.id.error_view) ErrorView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypedArray accentColor = obtainStyledAttributes(new TypedValue().data, new int[]{R.attr.colorAccent});
        tabLayout.setTabTextColors(0xB3FFFFFF, /*accentColor.getColor(0, */Color.WHITE/*)*/); // 70% white
        accentColor.recycle();

        progressBar.setIndeterminate(true);

        loginErrorConfig = ErrorView.Config.create()
                .image(tr.xip.errorview.R.drawable.error_view_cloud)
                .title("Server akzeptiert Logindaten nicht!")
                .titleColor(Color.WHITE)
                .retryVisible(true)
                .retryText("EINSTELLUNGEN")
                .retryTextColor(getResources().getColor(R.color.pinkAccent200))
                .build();

        internetErrorConfig = ErrorView.Config.create()
                .image(tr.xip.errorview.R.drawable.error_view_cloud)
                .title("Konnte Vertretungsplan nicht laden")
                .titleColor(Color.WHITE)
                .retryVisible(true)
                .retryText("ERNEUT VERSUCHEN")
                .retryTextColor(getResources().getColor(R.color.pinkAccent200))
                .build();

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadVertretungsplan();
    }

    private void loadVertretungsplan() {
        VertretungsplanManager.downloadAndSave(this,
                LoginManager.load(PreferenceManager.getDefaultSharedPreferences(this)), eventManager);
    }

    public void onDownloadStarted(@Observes VertretungsplanDownloadStartedEvent event) {
        setUiState(UI_STATE.LOADING);
    }

    public void onVertretungsplanDownloadError(@Observes VertretungsplanDownloadError event) {
        if (event.getException() instanceof LoginException) {
            setUiState(UI_STATE.LOGIN_ERROR);
        } else {
            setUiState(UI_STATE.INTERNET_ERROR);
        }
    }

    public void onVertretungsplanSaved(@Observes VertretungsplanSavedEvent event) {
        showVertretungsplan(event.getVertretungsplan());
    }

    public void showVertretungsplan(@NotNull Vertretungsplan vertretungsplan) {
        Profile profile = ProfileManager
                .load(PreferenceManager.getDefaultSharedPreferences(this));
        if (profile == null) {
            //todo: should never happen
            new AlertDialog.Builder(this).setMessage("Profil in den Einstellungen erstellen!").create().show();
            return;
        }

        PagerAdapter pagerAdapter = viewPager.getAdapter();
        DayPagerAdapter dayPagerAdapter;
        if (pagerAdapter == null) {
            dayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager());
        } else {
            dayPagerAdapter = (DayPagerAdapter) pagerAdapter;
        }
        dayPagerAdapter.setProfile(profile);
        dayPagerAdapter.setVertretungsplan(vertretungsplan);

        viewPager.setAdapter(dayPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setUiState(UI_STATE.SHOWING_VERTRETUNGSPLAN);

        toolbar.setTitle("Vertretungsplan - " + profile.toString());
    }

    enum UI_STATE {
        LOADING, INTERNET_ERROR, LOGIN_ERROR, SHOWING_VERTRETUNGSPLAN
    }

    /**
     * Changes the Ui appropriate to the given UI_STATE
     * @param state UI_STATE how the Ui will be changed
     */
    private void setUiState(UI_STATE state) {
        viewPager.setVisibility(state == UI_STATE.SHOWING_VERTRETUNGSPLAN ? View.VISIBLE : View.INVISIBLE);
        progressBar.setVisibility(state == UI_STATE.LOADING ? View.VISIBLE : View.INVISIBLE);
        errorView.setVisibility(state == UI_STATE.INTERNET_ERROR || state == UI_STATE.LOGIN_ERROR
                ? View.VISIBLE : View.INVISIBLE);

        switch (state) {
            case INTERNET_ERROR:
                // animating to dark background because error image(cloud) is white
                animateBackgroundColor(
                        rootView,
                        ((ColorDrawable) rootView.getBackground()).getColor(),
                        getResources().getColor(R.color.greyBackground),
                        500
                ).start();

                errorView.setConfig(internetErrorConfig);
                errorView.setOnRetryListener(new ErrorView.RetryListener() {
                    @Override
                    public void onRetry() {
                        loadVertretungsplan();
                    }
                });

                tabLayout.removeAllTabs();
                break;
            case LOADING:
                tabLayout.removeAllTabs();
                break;
            case SHOWING_VERTRETUNGSPLAN:
                // animating back to white background if there is no error image
                animateBackgroundColor(
                        rootView,
                        ((ColorDrawable) rootView.getBackground()).getColor(),
                        Color.WHITE,
                        0
                ).start();
                break;
            case LOGIN_ERROR:
                animateBackgroundColor(
                        rootView,
                        ((ColorDrawable) rootView.getBackground()).getColor(),
                        getResources().getColor(R.color.greyBackground),
                        500
                ).start();

                errorView.setConfig(loginErrorConfig);
                errorView.setOnRetryListener(new ErrorView.RetryListener() {
                    @Override
                    public void onRetry() {
                        SettingsFragment.showLoginDialog(MainActivity.this, getSupportFragmentManager(),
                                LoginManager.load(PreferenceManager
                                        .getDefaultSharedPreferences(MainActivity.this)));
                    }
                });
                break;
            default:
        }
    }

    /**
     * Returns a set up ValueAnimator for animating the background color of the given view
     * @param view View whose background color will be changed
     * @param fromColor Starting color
     * @param toColor End color
     * @param startDelay Delay after calling ValueAnimator#start() the animation will be started
     * @return The set up ValueAnimator
     */
    private ValueAnimator animateBackgroundColor(final View view, int fromColor, int toColor,
                                                 int startDelay) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        colorAnimation.setStartDelay(startDelay);
        return colorAnimation;
    }

    public void onLoginUpdated(@Observes LoginUpdatedEvent event) {
        // Should only be called when LoginDialog is opened from Snackbar
        LoginManager.save(PreferenceManager.getDefaultSharedPreferences(this), event.getLogin());
        loadVertretungsplan(); // Retry loading after Login changed
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_announcments:
                startActivity(new Intent(this, AnnouncementsActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
