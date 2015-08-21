package de.randombyte.sglvertretungsplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import de.randombyte.sglvertretungsplan.models.Kurs;

/**
 * Edit a Kurs in a DialogFragment
 * Using normal DialogFragment because I want to use the onCreateDialog(),
 * injection doesn't work with it, so it would be useless to extend from RoboGuice
 */
public class EditKursDialog extends DialogFragment {

    public static final String TAG = "editKursDialog";
    public static final String ARGS_KURS = "args_kurs";

    public static final int REQUEST_CODE_GET_KURS = 10;

    public static EditKursDialog newInstance(Kurs kurs) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_KURS, kurs);

        EditKursDialog fragment = new EditKursDialog();
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
        final TextInputLayout numberInputLayout =
                (TextInputLayout) rootView.findViewById(R.id.number_text_input_layout);
        final TextInputLayout fachInputLayout =
                (TextInputLayout) rootView.findViewById(R.id.fach_text_input_layout);
        final RadioButton gkRadio = (RadioButton) rootView.findViewById(R.id.grundkurs_radio);
        RadioButton lkRadio = (RadioButton) rootView.findViewById(R.id.leistungskurs_radio);

        int number = kurs.getNummer();

        //Number
        numberInputLayout.getEditText().setText(
                number < 0 ?
                        "" :
                        String.valueOf(kurs.getNummer()));
        final TextWatcher numberTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                numberInputLayout.setError(s.length() == 0 ? "Kursnummer eingeben!" : "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        numberInputLayout.getEditText().addTextChangedListener(numberTextWatcher);

        //Fach
        fachInputLayout.getEditText().setText(kurs.getFach());
        final TextWatcher fachTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fachInputLayout.setError(s.length() == 0 ? "Fach eingeben!" : "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        fachInputLayout.getEditText().addTextChangedListener(fachTextWatcher);

        boolean isGrundkurs = kurs.isGrundkurs();
        gkRadio.setChecked(isGrundkurs);
        lkRadio.setChecked(!isGrundkurs);

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String numberText = numberInputLayout.getEditText().getText().toString();
                        String fachText = fachInputLayout.getEditText().getText().toString();

                        //todo: Good?
                        if (numberText.isEmpty() || fachText.isEmpty()) {
                            numberTextWatcher.onTextChanged(numberText, 0, 0, 0);
                            fachTextWatcher.onTextChanged(fachText, 0, 0, 0);
                            return; //Dialog stays open
                        }

                        kurs.setGrundkurs(gkRadio.isChecked());
                        kurs.setNummer(Integer.parseInt(numberText));
                        kurs.setFach(fachText.toUpperCase());

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
