package net.eve0415.spigot.WebsocketIntegration.Util;

public enum WSIEventState {
    STARTING("STARTING"), STOPPING("STOPPING"), STATUS("STATUS"), CHAT("CHAT"), ADVANCEMENT("ADVANCEMENT"),
    SERVERINFO("SERVERINFO"), LOG("LOG"),
    // For Forge Server
    CONSTRUCTING("CONSTRUCTING"), PREINITIALIZATION("PREINITIALIZATION"), INITIALIZATION("INITIALIZATION"),
    POSTINITIALIZATION("POSTINITIALIZATION"), LOADCOMPLETE("LOADCOMPLETE"), ABOUTTOSTART("ABOUTTOSTART");

    private final String value;

    private WSIEventState(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
