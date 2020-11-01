package net.eve0415.spigot.WebsocketIntegration.Util;

public enum WSIEventState {
    STARTING("STARTING"),
    STOPPING("STOPPING"),
    STATUS("STATUS"),
    CHAT("CHAT"),
    ADVANCEMENT("ADVANCEMENT"),
    SERVERINFO("SERVERINFO");

    private final String value;

    private WSIEventState(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
