package net.eve0415.spigot.WebsocketIntegration.Util;

public enum WSIEventState {
    STARTING("STARTING"), STOPPING("STOPPING"), STATUS("STATUS"), CHAT("CHAT"), ADVANCEMENT("ADVANCEMENT"),
    SERVERINFO("SERVERINFO"), LOG("LOG"),
    // For Forge Server
    CONSTRUCTING("Constructing"), PreInitialization("Pre-Initializing"), Initialization("Initializing"),
    PostInitialization("Post-Initializing"), LoadComplete("Load Complete"), AboutToStart("Loading World");

    private final String value;

    private WSIEventState(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
