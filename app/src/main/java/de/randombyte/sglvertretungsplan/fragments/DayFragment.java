package de.randombyte.sglvertretungsplan.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.randombyte.sglvertretungsplan.R;
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
    private @InjectView(R.id.recycler_view) RecyclerView recyclerView;
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new VertretungsListAdapter(
                Vertretung.getFiltered(day.getVertretungList(), profile)));

        setDayEmpty(day.getVertretungList().size() == 0);
    }

    private void setDayEmpty(boolean empty) {
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        dayEmptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }
}
