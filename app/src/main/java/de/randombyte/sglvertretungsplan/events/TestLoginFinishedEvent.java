package de.randombyte.sglvertretungsplan.events;

public class TestLoginFinishedEvent {

    private final boolean success;

    public TestLoginFinishedEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
