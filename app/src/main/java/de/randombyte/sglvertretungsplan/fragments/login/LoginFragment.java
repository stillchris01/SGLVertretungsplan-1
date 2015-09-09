package de.randombyte.sglvertretungsplan.fragments.login;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;

import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.events.LoginUpdatedEvent;
import de.randombyte.sglvertretungsplan.events.TestLoginFinishedEvent;
import de.randombyte.sglvertretungsplan.events.TestLoginStartEvent;
import de.randombyte.sglvertretungsplan.models.Login;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.util.RoboAsyncTask;

/**
 * NOT UP-TO-DATE! Split into two parts: The Fragment itself(EditTexts and ProgressBar) and the Dialog(login Button
 * as positive button).
 * When shown as a Dialog it fires the TestLoginStartEvent when the positive button is clicked.
 * To use this fragment in an Activity you have to fire TestLoginStartEvent, e.g. when a login
 * utton is pressed in the Activity.
 * The TestLoginStartEvent is caught by the Fragment which will execute the login and report the
 * result back via the TestLoginFinishedEvent. The Dialog will close when login was successful.
 *
 * @author randombyte-developer
 */
public class LoginFragment extends Fragment {

    public static final String TAG = "loginFragment";

    public static final String ARGS_LOGIN = "args_profile";
    public static final String ARGS_IS_INTRO = "args_is_intro";

    private @Inject EventManager eventManager;

    private Login login;
    private boolean isIntro;

    private EditText username;
    private EditText password;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout usernameInputLayout;

    private ProgressBar progress;
    private TextView loginStatus;
    private AsyncTask runningTask;

    public static LoginFragment newInstance(@Nullable Login login, boolean isIntro) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_LOGIN, login);
        args.putBoolean(ARGS_IS_INTRO, isIntro);

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            login = savedInstanceState.getParcelable(ARGS_LOGIN);
        } else {
            login = getArguments().getParcelable(ARGS_LOGIN);
            if (login == null) {
                login = new Login("","");
            }
            isIntro = getArguments().getBoolean(ARGS_IS_INTRO, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {
        username = (EditText) rootView.findViewById(R.id.login_username);
        usernameInputLayout = (TextInputLayout) rootView.findViewById(R.id.user_input_layout);
        password = (EditText) rootView.findViewById(R.id.login_password);
        passwordInputLayout = (TextInputLayout) rootView.findViewById(R.id.password_input_layout);
        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        loginStatus = (TextView) rootView.findViewById(R.id.login_status);

        if (isIntro) {
            username.setTextColor(Color.WHITE);
            password.setTextColor(Color.WHITE);
            usernameInputLayout.setHintTextAppearance(R.style.LightTextHintAppearance);
            passwordInputLayout.setHintTextAppearance(R.style.LightTextHintAppearance);
        }
        username.setText(login.getUsername());
        password.setText(login.getPassword());
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_GO) {
                    eventManager.fire(new TestLoginStartEvent());
                    return true;
                }

                return false;
            }
        });
        progress.setIndeterminate(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        runningTask = null; // Cancel the hard way?!

        Login login = constructLogin();
        eventManager.fire(new LoginUpdatedEvent(login));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARGS_LOGIN, constructLogin());
    }

    private Login constructLogin() {
        login = new Login(
                username.getEditableText().toString(),
                password.getEditableText().toString());
        return login;
    }

    public void onStartTestLogin(@Observes TestLoginStartEvent event) {

        if (getActivity() == null) {
            // Clicked login button and instantly rotated device
            return;
        }

        new TestLogin(getActivity(), constructLogin()) {
            @Override
            protected void onPreExecute() {
                progress.setVisibility(View.VISIBLE);
                loginStatus.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onSuccess(Boolean success) throws Exception {
                if (getActivity() != null) {
                    progress.setVisibility(View.INVISIBLE);
                    loginStatus.setText(success ? "Login erfolgreich!" : "Login fehlgeschlagen!");
                    loginStatus.setVisibility(View.VISIBLE);
                    eventManager.fire(new TestLoginFinishedEvent(success));
                }
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                if (getActivity() != null) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Ein Fehler ist aufgetreten!\n" + e.getMessage()).create().show();
                }
            }
        }.execute();
    }

    private static class TestLogin extends RoboAsyncTask<Boolean> {

        private final Login login;

        protected TestLogin(Context context, Login login) {
            super(context);
            this.login = login;
        }

        /**
         * @return Success logging in with given login
         */
        @Override
        public Boolean call() throws Exception {
            return !login.loadLinks()[0].getUrl().contains("NoContent");
        }
    }
}
