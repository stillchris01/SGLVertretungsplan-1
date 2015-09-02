package de.randombyte.sglvertretungsplan;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URL;

import de.randombyte.sglvertretungsplan.models.Announcement;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

@ContentView(R.layout.activity_announcements)
public class AnnouncementsActivity extends RoboActionBarActivity {

    private @InjectView(R.id.toolbar) Toolbar toolbar;
    private @InjectView(R.id.announcements_textview) TextView textView;
    private @InjectView(R.id.progress) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new LoadAnnouncementsAsyncTask(this) {
            @Override
            protected void onPreExecute() throws Exception {
                progressBar.setIndeterminate(true);
                setLoadingUi(true);
            }

            @Override
            protected void onSuccess(String s) throws Exception {
                setLoadingUi(false);
                textView.setText(s);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                setLoadingUi(false);
                textView.setText("Ein Fehler ist aufgetreten!");
            }
        }.execute();
    }

    private void setLoadingUi(boolean loading) {
        textView.setVisibility(loading ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    class LoadAnnouncementsAsyncTask extends RoboAsyncTask<String> {

        private static final String ANNOUNCEMENTS_URL =
                "https://cdn.gitcdn.xyz/cdn/randombyte-developer/SGLVertretungsplan/" +
                        "72ba285a56ceacb9e34338cb00bbcde496f3148f/announcements.json";

        protected LoadAnnouncementsAsyncTask(Context context) {
            super(context);
        }

        @Override
        public String call() throws Exception {

            InputStreamReader reader = null;
            Announcement[] announcements;
            try {
                reader = new InputStreamReader(new URL(ANNOUNCEMENTS_URL).openStream());
                announcements = new Gson().fromJson(reader, Announcement[].class);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (Announcement announcement : announcements) {
                stringBuilder
                        .append("---\n")
                        .append(announcement.getHeader())
                        .append("\n---\n\n")
                        .append(announcement.getMessage())
                        .append("\n\n\n");
            }

            return stringBuilder.toString();
        }
    }
}
