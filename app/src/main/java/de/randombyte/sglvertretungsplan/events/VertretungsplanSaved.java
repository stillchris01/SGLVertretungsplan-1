package de.randombyte.sglvertretungsplan.events;

import de.randombyte.sglvertretungsplan.models.Vertretungsplan;

public class VertretungsplanSaved {

    private final Vertretungsplan vertretungsplan;

    public VertretungsplanSaved(Vertretungsplan vertretungsplan) {
        this.vertretungsplan = vertretungsplan;
    }

    public Vertretungsplan getVertretungsplan() {
        return vertretungsplan;
    }
}
