package net.eve0415.spigot.WebsocketIntegration.Util;

public enum WSIPlatformType {
    Paper("PaperMC"),
    Sponge("Sponge"),
    Velocity("Velocity");

    private final String platformName;

    private WSIPlatformType(final String value) {
        this.platformName = value;
    }

    public String getValue() {
        return this.platformName;
    }
}
