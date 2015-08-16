package de.randombyte.sglvertretungsplan.models;

public class Vertretung {

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
}
