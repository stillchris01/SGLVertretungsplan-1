package de.randombyte.sglvertretungsplan.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Vertretung implements Parcelable {

    private String zeitraum;
    private String klasse;
    private String vertreter;
    private String statt;
    private String fach;
    private String raum;
    private String verlegung;
    private String art;
    private String zusatzinfo;

    public Vertretung() {
    }

    private Vertretung(Parcel source) {
        zeitraum = source.readString();
        klasse = source.readString();
        vertreter = source.readString();
        statt = source.readString();
        fach = source.readString();
        raum = source.readString();
        verlegung = source.readString();
        art = source.readString();
        zusatzinfo = source.readString();
    }

    public String getZeitraum() {
        return zeitraum;
    }

    public void setZeitraum(String zeitraum) {
        this.zeitraum = zeitraum;
    }

    public String getKlasse() {
        return klasse;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }

    public String getVertreter() {
        return vertreter;
    }

    public void setVertreter(String vertreter) {
        this.vertreter = vertreter;
    }

    public String getStatt() {
        return statt;
    }

    public void setStatt(String statt) {
        this.statt = statt;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    public String getVerlegung() {
        return verlegung;
    }

    public void setVerlegung(String verlegung) {
        this.verlegung = verlegung;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getZusatzinfo() {
        return zusatzinfo;
    }

    public void setZusatzinfo(String zusatzinfo) {
        this.zusatzinfo = zusatzinfo;
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Vertretung> CREATOR = new Creator<Vertretung>() {
        @Override
        public Vertretung createFromParcel(Parcel source) {
            return new Vertretung(source);
        }

        @Override
        public Vertretung[] newArray(int size) {
            return new Vertretung[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(zeitraum);
        dest.writeString(klasse);
        dest.writeString(vertreter);
        dest.writeString(statt);
        dest.writeString(fach);
        dest.writeString(raum);
        dest.writeString(verlegung);
        dest.writeString(art);
        dest.writeString(zusatzinfo);
    }

    public static List<Vertretung> getFiltered(List<Vertretung> vertretungList, Profile profile) {

        List<Vertretung> filteredList = new ArrayList<>();

        for (Vertretung vertretung : vertretungList) {
            if (profile.isOberstufe()) {
                for (Kurs kurs : profile.getKursList()) {
                    if (vertretung.getFach().equalsIgnoreCase(kurs.toString())) {
                        filteredList.add(vertretung);
                    }
                }

            } else {
                if (vertretung.getKlasse().equalsIgnoreCase(profile.toString())) {
                    filteredList.add(vertretung);
                }
            }
        }

        return filteredList;
    }
}
