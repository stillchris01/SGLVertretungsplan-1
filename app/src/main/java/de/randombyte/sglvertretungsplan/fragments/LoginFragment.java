package de.randombyte.sglvertretungsplan.fragments;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.google.inject.Inject;

import java.io.IOException;

import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.events.LoginUpdatedEvent;
import de.randombyte.sglvertretungsplan.events.TestLoginFinishedEvent;
import de.randombyte.sglvertretungsplan.models.Login;
import roboguice.event.EventManager;

public class LoginFragment extends DialogFragment {

    public static final String TAG = "loginFragment";

    public static final String ARGS_LOGIN = "args_profile";

    private @Inject EventManager eventManager;

    private Login login;

    private CircularProgressButton loginButton;

    public static LoginFragment newInstance(Login login) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_LOGIN, login);

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login = getArguments().getParcelable(ARGS_LOGIN);
        if (login == null) {
            login = new Login("","");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {

        ((EditText) rootView.findViewById(R.id.login_username)).setText(login.getUsername());
        ((EditText) rootView.findViewById(R.id.login_password)).setText(login.getPassword());

        loginButton = (CircularProgressButton) rootView.findViewById(R.id.login_button);

        loginButton.setIndeterminateProgressMode(true);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestLogin() {

                    @Override
                    protected void onPreExecute() {
                        loginButton.setProgress(50);
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {
                        loginButton.setProgress(success ? 100 : -1);
                        eventManager.fire(new TestLoginFinishedEvent(success));
                    }
                }.execute(constructLogin(rootView));
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        Login login = constructLogin(getView());
        eventManager.fire(new LoginUpdatedEvent(login));
    }

    private Login constructLogin(View rootView) {
        return (new Login(
                ((EditText) rootView.findViewById(R.id.login_username)).getEditableText().toString(),
                ((EditText) rootView.findViewById(R.id.login_password)).getEditableText().toString()));
    }

    private static class TestLogin extends AsyncTask<Login, Void, Boolean> {
        /**
         * @return Success logging in with given login
         */
        @Override
        protected Boolean doInBackground(Login... login) {
            try {
                return !login[0].loadLinks()[0].getUrl().contains("NoContent");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
