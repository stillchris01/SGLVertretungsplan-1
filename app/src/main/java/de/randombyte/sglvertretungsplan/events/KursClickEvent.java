package de.randombyte.sglvertretungsplan.events;

import de.randombyte.sglvertretungsplan.models.Kurs;

public class KursClickEvent {

    private final Kurs kurs;

    public KursClickEvent(Kurs kurs) {
        this.kurs = kurs;
    }

    public Kurs getKurs() {
        return kurs;
    }
}
