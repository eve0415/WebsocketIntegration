package net.eve0415.spigot.WebsocketIntegration.Util;

public enum PlatformType {
    Paper("PaperMC");

    private final String platformName;

    private PlatformType(final String value) {
        this.platformName = value;
    }

    public String getValue() {
        return this.platformName;
    }
}
