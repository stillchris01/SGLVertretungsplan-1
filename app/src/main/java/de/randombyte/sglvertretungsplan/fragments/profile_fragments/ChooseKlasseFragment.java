package de.randombyte.sglvertretungsplan.fragments.profile_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.inject.Inject;

import java.util.Arrays;

import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.events.KlasseChooseEvent;
import roboguice.event.EventManager;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ChooseKlasseFragment extends RoboFragment {

    public static final String ARGS_KLASSE = "args_klasse";

    private @Inject EventManager eventManager;

    private @InjectView(R.id.spinner_klasse) Spinner klasseSpinner;

    private String klasse;

    public static ChooseKlasseFragment newInstance(String klasse) {

        Bundle args = new Bundle();
        args.putString(ARGS_KLASSE, klasse);

        ChooseKlasseFragment fragment = new ChooseKlasseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        klasse = getArguments().getString(ARGS_KLASSE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_klasse, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        klasseSpinner.setSelection(klasseToSpinnerPos(getActivity(), klasse));
        klasseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventManager.fire(new KlasseChooseEvent(parent.getSelectedItem().toString())); //e.g. "C"
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private static int klasseToSpinnerPos(Context context, String klasse) {
        return Arrays.asList(context.getResources().getStringArray(R.array.spinner_klasse_entries))
                .indexOf(klasse);
    }
}
