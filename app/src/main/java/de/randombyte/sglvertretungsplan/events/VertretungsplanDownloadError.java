package de.randombyte.sglvertretungsplan.events;

public class VertretungsplanDownloadError {

    private final Exception exception;

    public VertretungsplanDownloadError(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
