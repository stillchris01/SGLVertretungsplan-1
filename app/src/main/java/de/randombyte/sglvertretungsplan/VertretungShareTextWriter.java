package de.randombyte.sglvertretungsplan;

import java.util.List;

import de.randombyte.sglvertretungsplan.models.Profile;
import de.randombyte.sglvertretungsplan.models.Vertretung;

public class VertretungShareTextWriter {

    /**
     * Writes the given vertretungList and other info in the given StringBuilder
     * @param vertretungList List of Vertretung that will be written
     * @param day The day name(e.g. "Dienstag")
     * @param profile If profile not "Oberstufe" then the name of the Profile will be appended("9a")
     * @param stringBuilder The StringBuilder to write into
     * @return The given StringBuilder for better chaining calls
     */
    public static StringBuilder write(List<Vertretung> vertretungList, String day,
                                         Profile profile, StringBuilder stringBuilder) {
        stringBuilder.append(day); // Montag
        if (!profile.isOberstufe()) {
            stringBuilder.append(" ").append(profile.toString()); // _9a
        }
        stringBuilder.append(": "); // :_
        writeVertretungList(vertretungList, stringBuilder);

        return stringBuilder;
    }

    private static StringBuilder writeVertretungList(List<Vertretung> vertretungList, StringBuilder stringBuilder) {
        if (vertretungList.size() == 0) {
            return stringBuilder.append("Keine Vertretung");
        }

        for (int i = 0; i < vertretungList.size(); i++) {
            if (i != 0) stringBuilder.append(" | ");
            writeVertretung(vertretungList.get(i), stringBuilder);
        }

        return stringBuilder;
    }

    private static StringBuilder writeVertretung(Vertretung vertretung, StringBuilder stringBuilder) {
        stringBuilder.append(vertretung.getZeitraum()).append(" Stunde(")
                .append(vertretung.getFach()).append("): "); // 1-2 Stunde(GK07-D):_

        switch (vertretung.parseArt()) {
            case BETREUUNG:
                return stringBuilder.append(vertretung.getVertreter()).append(" betreut");
            case RAUM_VTR:
                return stringBuilder.append("Im Raum ").append(vertretung.getRaum());
            case TROTZ_ABSENZ:
                return stringBuilder.append("Trotz Absenz bei ").append(vertretung.getVertreter());
            case STATT_VERTRETUNG:
                return stringBuilder.append("Statt-Vertretung bei ")
                        .append(vertretung.getVertreter());
            case VERTRETUNG:
                return stringBuilder.append("Vertretung bei ").append(vertretung.getVertreter());
            case LEHRERTAUSCH:
                return stringBuilder.append(vertretung.getVertreter()).append(" tauscht mit ")
                        .append(vertretung.getStatt());
            case VORMERKUNG:
                return stringBuilder.append("Vormerkung von ").append(vertretung.getVertreter());
            case EIGENVERANTWORTLICHES_ARBEITEN:
                return stringBuilder.append("Eigenverantwortliches Arbeiten");
            case CUSTOM:
            default:
                // Not handled specifically: ENTFALL, VERLEGUNG
                return stringBuilder.append(vertretung.getArt());
        }
    }
}
