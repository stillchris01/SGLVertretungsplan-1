package de.randombyte.sglvertretungsplan.models;

public class Kurs {

    private int id;
    private boolean grundkurs;
    private int nummer;
    private String fach;

    public Kurs() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isGrundkurs() {
        return grundkurs;
    }

    public void setGrundkurs(boolean grundkurs) {
        this.grundkurs = grundkurs;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }
}
