package de.randombyte.sglvertretungsplan.events;

public class KursClickEvent {

    private long creationDate; //Good for identifying a Kurs

    public KursClickEvent(long kurs) {
        this.creationDate = kurs;
    }

    public long getCreationDate() {
        return creationDate;
    }
}
