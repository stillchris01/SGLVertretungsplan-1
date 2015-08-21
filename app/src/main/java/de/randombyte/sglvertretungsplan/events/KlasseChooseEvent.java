package de.randombyte.sglvertretungsplan.events;

public class KlasseChooseEvent {

    private String klasse;

    public KlasseChooseEvent(String klasse) {
        this.klasse = klasse;
    }

    public String getKlasse() {
        return klasse;
    }
}
