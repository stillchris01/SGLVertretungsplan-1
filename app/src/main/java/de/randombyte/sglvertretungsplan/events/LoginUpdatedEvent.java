package de.randombyte.sglvertretungsplan.events;

import de.randombyte.sglvertretungsplan.models.Login;

/**
 * Fired when LoginFragment is onPause()
 */
public class LoginUpdatedEvent {

    private final Login login;

    public LoginUpdatedEvent(Login login) {
        this.login = login;
    }

    public Login getLogin() {
        return login;
    }
}
