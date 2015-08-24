package de.randombyte.sglvertretungsplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Arrays;

import de.randombyte.sglvertretungsplan.customviews.VerticalSwitcher;
import de.randombyte.sglvertretungsplan.models.Kurs;

/**
 * Edit a Kurs in a DialogFragment
 * Using normal DialogFragment because I want to use the onCreateDialog(),
 * injection doesn't work with it, so it would be useless to extend from RoboGuice
 */
public class NewEditKursDialog extends DialogFragment {

    public static final String TAG = "editKursDialog";
    public static final String ARGS_KURS = "args_kurs";

    public static final int REQUEST_CODE_GET_KURS = 10;

    public static NewEditKursDialog newInstance(Kurs kurs) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_KURS, kurs);

        NewEditKursDialog fragment = new NewEditKursDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_edit_kurs, null, false);

        final Kurs kurs = getArguments().getParcelable(ARGS_KURS);

        //Views
        final VerticalSwitcher kursNummerSwitcher = (VerticalSwitcher) rootView.findViewById(R.id.kurs_nummer_switcher);
        final VerticalSwitcher gkLkSwicther = (VerticalSwitcher) rootView.findViewById(R.id.gk_lk_switcher);
        final VerticalSwitcher fachSwitcher = (VerticalSwitcher) rootView.findViewById(R.id.fach_switcher);

        kursNummerSwitcher.setIndex(kurs.getNummer() -1); //hacky
        gkLkSwicther.setIndex(kurs.isGrundkurs() ? 0 : 1); //hacky
        fachSwitcher.setIndex(
                Arrays.asList(fachSwitcher.getEntries()).indexOf(kurs.getFach())); //more hacky :/

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        kurs.setGrundkurs(gkLkSwicther.getIndex() == 0); //First entry is GK
                        kurs.setNummer(Integer.parseInt(kursNummerSwitcher.getSelectedEntry()));
                        kurs.setFach(fachSwitcher.getSelectedEntry().toUpperCase());

                        Intent intentWithKursData = new Intent();
                        intentWithKursData.putExtra(ARGS_KURS, kurs);

                        getTargetFragment().onActivityResult(REQUEST_CODE_GET_KURS,
                                Activity.RESULT_OK, intentWithKursData);
                        dialog.dismiss();
                    }
                })
                .create();
    }
}