package de.randombyte.sglvertretungsplan.fragments.login;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.randombyte.sglvertretungsplan.CredentialsManager;
import de.randombyte.sglvertretungsplan.R;
import roboguice.RoboGuice;
import roboguice.fragment.RoboFragment;

public class LoginIntroFragment extends RoboFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_intro, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        LoginFragment loginFragment = LoginFragment.newInstance(
                CredentialsManager.load(PreferenceManager
                        .getDefaultSharedPreferences(getActivity())), true);
        RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(loginFragment);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_content, loginFragment)
                .commit();
    }
}
