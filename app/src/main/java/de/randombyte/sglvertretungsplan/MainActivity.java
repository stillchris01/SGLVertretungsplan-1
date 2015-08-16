package de.randombyte.sglvertretungsplan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;

import de.randombyte.sglvertretungsplan.events.TestButtonClicked;
import de.randombyte.sglvertretungsplan.events.VertretungsplanDownloadError;
import de.randombyte.sglvertretungsplan.events.VertretungsplanSaved;
import de.randombyte.sglvertretungsplan.models.Day;
import de.randombyte.sglvertretungsplan.models.Login;
import de.randombyte.sglvertretungsplan.models.Vertretung;
import de.randombyte.sglvertretungsplan.models.Vertretungsplan;
import roboguice.activity.RoboActionBarActivity;
import roboguice.activity.RoboActivity;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity {

    @Inject EventManager eventManager;

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.testing_button) Button button;
    @InjectView(R.id.test_textview) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setTitle("Testing");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventManager.fire(new TestButtonClicked());
            }
        });

    }

    public void onTestButtonClick(@Observes TestButtonClicked clicked) {
        VertretungsplanManager.downloadAndSave(this, new Login("bla", "blub"), eventManager);
        textView.setText("Loading");
    }

    public void onVertretungsplanSaved(@Observes VertretungsplanSaved event) {
        String text = "";
        for (Day day : event.getVertretungsplan().getDays()) {
            text += day.getDayName() + ":\n";
            for (Vertretung vertretung : day.getVertretungList()) {
                text += vertretung.getKlasse() + ", ";
            }
        }
        textView.setText(text);
    }

    public void onVertretungsplanDownloadError(@Observes VertretungsplanDownloadError event) {
        textView.setText("Error:\n" + event.getException().getMessage());
    }
}
