package de.randombyte.sglvertretungsplan.events;

import de.randombyte.sglvertretungsplan.models.Credentials;

/**
 * Fired when LoginFragment is onPause()
 */
public class LoginUpdatedEvent {

    private final Credentials credentials;

    public LoginUpdatedEvent(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
