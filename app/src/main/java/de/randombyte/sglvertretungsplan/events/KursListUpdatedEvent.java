package de.randombyte.sglvertretungsplan.events;

import java.util.List;

import de.randombyte.sglvertretungsplan.models.Kurs;

/**
 * Fired when the kursList was updated in EditKursListFragment
 */
public class KursListUpdatedEvent {

    private final List<Kurs> kursList;

    public KursListUpdatedEvent(List<Kurs> kursList) {
        this.kursList = kursList;
    }

    public List<Kurs> getKursList() {
        return kursList;
    }
}
