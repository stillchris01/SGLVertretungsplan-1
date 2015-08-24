package de.randombyte.sglvertretungsplan.events;

public class KursDeleteEvent {

    private final long creationDate; //Good for identifying a Kurs

    public KursDeleteEvent(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getCreationDate() {
        return creationDate;
    }
}
