package de.randombyte.sglvertretungsplan.exceptions;

/**
 * Utility class
 */
public class Exceptions {

    public static void throwIfNull(Object object, RuntimeException exception) {
        if (object == null) {
            throw exception;
        }
    }
}
