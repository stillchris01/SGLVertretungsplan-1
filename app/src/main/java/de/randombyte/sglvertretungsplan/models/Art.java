package de.randombyte.sglvertretungsplan.models;

public enum Art {
    BETREUUNG("Betreuung"),
    ENTFALL("Entfall"),
    VERTRETUNG("Vertretung"),
    STATT_VERTRETUNG("Statt-Vertretung"),
    VERLEGUNG("Verlegung"),
    TROTZ_ABSENZ("Trotz Absenz"),
    RAUM_VTR("Raum-Vtr."),
    LEHRERTAUSCH("Lehrertausch"),
    EIGENVERANTWORTLICHES_ARBEITEN("eigenverant. Arbeiten"),
    VORMERKUNG("Vormerkung"),

    CUSTOM("*");

    private final String text;

    private Art(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Art fromString(String text) {
        if (text == null) throw new IllegalArgumentException("text is null!");

        for (Art art : Art.values()) {
            if (art.text.equalsIgnoreCase(text)) {
                return art;
            }
        }

        return CUSTOM;
    }
}
