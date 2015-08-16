package de.randombyte.sglvertretungsplan.models;

import java.util.List;

public class Profile {

    private int id;
    private boolean oberstufe;
    private String stufe;
    private String suffix;
    private List<Kurs> kursList;

    public Profile() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOberstufe() {
        return oberstufe;
    }

    public void setOberstufe(boolean oberstufe) {
        this.oberstufe = oberstufe;
    }

    public String getStufe() {
        return stufe;
    }

    public void setStufe(String stufe) {
        this.stufe = stufe;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<Kurs> getKursList() {
        return kursList;
    }

    public void setKursList(List<Kurs> kursList) {
        this.kursList = kursList;
    }
}
