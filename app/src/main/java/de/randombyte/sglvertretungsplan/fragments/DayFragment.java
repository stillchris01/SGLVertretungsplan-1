package de.randombyte.sglvertretungsplan.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.VertretungShareTextWriter;
import de.randombyte.sglvertretungsplan.adapters.VertretungsListAdapter;
import de.randombyte.sglvertretungsplan.models.Day;
import de.randombyte.sglvertretungsplan.models.Profile;
import de.randombyte.sglvertretungsplan.models.Vertretung;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class DayFragment extends RoboFragment {

    public static final String ARGS_DAY = "args_day";
    public static final String ARGS_PROFILE = "args_profile";

    private @InjectView(R.id.date) TextView date;
    private @InjectView(R.id.motd_button) Button motdButton;
    private @InjectView(R.id.recycler_view) RecyclerView recyclerView;
    private @InjectView(R.id.fab) FloatingActionButton shareFab;
    private @InjectView(R.id.day_empty) View dayEmptyView;

    private Day day;
    private Profile profile;

    public static DayFragment newInstance(Day day, Profile profile) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_DAY, day);
        args.putParcelable(ARGS_PROFILE, profile);

        DayFragment fragment = new DayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        day = (Day) getArguments().get(ARGS_DAY);
        profile = (Profile) getArguments().get(ARGS_PROFILE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        date.setText(day.getDate());
        final List<Vertretung> filteredVertretungList =
                Vertretung.getFiltered(day.getVertretungList(), profile);

        motdButton.setVisibility(day.getMotd() == null || day.getMotd().isEmpty() ?
                View.GONE : View.VISIBLE);
        motdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Nachrichten zum Tag")
                        .setMessage(day.getMotd())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new VertretungsListAdapter(filteredVertretungList));

        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareVertretungTextIntent = new Intent(Intent.ACTION_SEND);
                shareVertretungTextIntent.setType("text/plain");
                shareVertretungTextIntent.putExtra(Intent.EXTRA_TEXT,
                        VertretungShareTextWriter.write(filteredVertretungList, day.getDayName(),
                                profile, new StringBuilder()).toString());
                startActivity(shareVertretungTextIntent);
            }
        });

        setDayEmpty(filteredVertretungList.size() == 0);
    }

    private void setDayEmpty(boolean empty) {
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        dayEmptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_day_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_open_in_browser:
                if (day.getDownloadedTimeStamp() + Day.URL_TIMEOUT >
                        Calendar.getInstance().getTimeInMillis()) {
                    // Should be valid
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(day.getTimetableInfo().getUrl()));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Bitte Vertretungsplan zuerst neu laden!",
                            Toast.LENGTH_LONG).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
