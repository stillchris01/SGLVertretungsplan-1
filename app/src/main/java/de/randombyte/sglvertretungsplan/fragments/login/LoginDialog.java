package de.randombyte.sglvertretungsplan.fragments.login;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.inject.Inject;

import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.events.TestLoginFinishedEvent;
import de.randombyte.sglvertretungsplan.events.TestLoginStartEvent;
import de.randombyte.sglvertretungsplan.models.Login;
import roboguice.RoboGuice;
import roboguice.event.EventManager;
import roboguice.event.Observes;

public class LoginDialog extends DialogFragment {

    public static final String TAG = "loginDialog";

    private @Inject EventManager eventManager;

    private Button loginButton;

    public static LoginDialog newInstance(Login login) {

        Bundle args = new Bundle();
        args.putParcelable(LoginFragment.ARGS_LOGIN, login);

        LoginDialog fragment = new LoginDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_login, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        LoginFragment fragment = new LoginFragment();
        RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(fragment);
        fragment.setArguments(getArguments());
        getChildFragmentManager().beginTransaction()
                .replace(R.id.login_fragment, fragment)
                .commit();

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        loginButton = (Button) rootView.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventManager.fire(new TestLoginStartEvent());
            }
        });
    }

    public void onStartTestLogin(@Observes TestLoginStartEvent event) {
        loginButton.setEnabled(false);
    }

    public void onFinishedTestLogin(@Observes TestLoginFinishedEvent event) {

        if (getActivity() != null) {
            if (event.isSuccess()) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity() != null) {
                            dismiss();
                        }
                    }
                }, 1000);
            } else {
                loginButton.setEnabled(true);
            }
        }
    }
}
