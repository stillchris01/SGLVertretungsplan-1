package de.randombyte.sglvertretungsplan.events;

import de.randombyte.sglvertretungsplan.models.Vertretungsplan;

public class VertretungsplanSavedEvent {

    private final Vertretungsplan vertretungsplan;

    public VertretungsplanSavedEvent(Vertretungsplan vertretungsplan) {
        this.vertretungsplan = vertretungsplan;
    }

    public Vertretungsplan getVertretungsplan() {
        return vertretungsplan;
    }
}
