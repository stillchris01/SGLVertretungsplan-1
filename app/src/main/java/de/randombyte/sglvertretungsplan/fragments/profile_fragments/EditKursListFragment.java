package de.randombyte.sglvertretungsplan.fragments.profile_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.randombyte.sglvertretungsplan.NewEditKursDialog;
import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.adapters.KursListAdapter;
import de.randombyte.sglvertretungsplan.events.KursClickEvent;
import de.randombyte.sglvertretungsplan.models.Kurs;
import roboguice.RoboGuice;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EditKursListFragment extends RoboFragment {

    public static final String ARGS_KURS_LIST = "args_kurs_list";

    private @Inject EventManager eventManager;

    private @InjectView(R.id.recycler_view) RecyclerView recyclerView;
    private @InjectView(R.id.fab) FloatingActionButton fab;

    private List<Kurs> kursList;

    public static EditKursListFragment newInstance(List<Kurs> kursList) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARGS_KURS_LIST, new ArrayList<Parcelable>(kursList));

        EditKursListFragment fragment = new EditKursListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kursList = getArguments().getParcelableArrayList(ARGS_KURS_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_kurs_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        KursListAdapter kursListAdapter = new KursListAdapter(kursList);
        RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(kursListAdapter); //For EventManager

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(kursListAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKursDialog(new Kurs(Calendar.getInstance().getTimeInMillis(), true, 1, "D", "")); //todo: rather give null instead of mock Kurs?
            }
        });
    }

    public void onKursClick(@Observes KursClickEvent event) {
        showKursDialog(event.getKurs());
    }

    private void showKursDialog(Kurs kurs) {
        NewEditKursDialog editKursDialog = NewEditKursDialog.newInstance(kurs);
        editKursDialog.setTargetFragment(this, NewEditKursDialog.REQUEST_CODE_GET_KURS);
        editKursDialog.show(getChildFragmentManager(), NewEditKursDialog.TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == NewEditKursDialog.REQUEST_CODE_GET_KURS && resultCode == Activity.RESULT_OK) {

            Kurs kurs = data.getParcelableExtra(NewEditKursDialog.ARGS_KURS);

            KursListAdapter adapter = (KursListAdapter) recyclerView.getAdapter();
            adapter.addOrUpdate(kurs);
        }
    }
}
