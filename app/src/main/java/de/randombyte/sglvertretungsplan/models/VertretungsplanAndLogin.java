package de.randombyte.sglvertretungsplan.models;

/**
 * For having one object(look for usages to understand)
 */
public class VertretungsplanAndLogin {

    private final Vertretungsplan vertretungsplan;
    private final Login login;

    public VertretungsplanAndLogin(Vertretungsplan vertretungsplan, Login login) {
        this.vertretungsplan = vertretungsplan;
        this.login = login;
    }

    public Vertretungsplan getVertretungsplan() {
        return vertretungsplan;
    }

    public Login getLogin() {
        return login;
    }
}
