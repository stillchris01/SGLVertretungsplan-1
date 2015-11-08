package de.randombyte.sglvertretungsplan.exceptions;

public class InvalidResponseException extends RuntimeException {

    public InvalidResponseException(String detailMessage) {
        super(detailMessage);
    }
}
