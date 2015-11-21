package de.randombyte.sglvertretungsplan.models;

/**
 * For having one object(look for usages to understand)
 */
public class VertretungsplanAndLogin {

    private final Vertretungsplan vertretungsplan;
    private final Credentials credentials;

    public VertretungsplanAndLogin(Vertretungsplan vertretungsplan, Credentials credentials) {
        this.vertretungsplan = vertretungsplan;
        this.credentials = credentials;
    }

    public Vertretungsplan getVertretungsplan() {
        return vertretungsplan;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
