package de.randombyte.sglvertretungsplan.models;

/**
 * For parsing the announcements with Gson
 */
public class Announcement {

    private int number;
    private String header;
    private String message;

    public int getNumber() {
        return number;
    }

    public String getHeader() {
        return header;
    }

    public String getMessage() {
        return message;
    }
}
