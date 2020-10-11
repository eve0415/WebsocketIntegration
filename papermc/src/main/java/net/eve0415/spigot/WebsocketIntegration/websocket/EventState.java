package net.eve0415.spigot.WebsocketIntegration.websocket;

public enum EventState {
    STARTING("STARTING"),
    STARTED("STARTED"),
    MESSAGE("MESSAGE"),
    COMMAND("COMMAND"),
    ACHIEVEMENT("ACHIEVEMENT"),
    JOIN("JOIN"),
    QUIT("QUIT"),
    DEATH("DEATH"),
    STOPPING("STOPPING"),
    STATUS("STATUS"),
    LINK("LINK");

    private final String value;

    private EventState(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
