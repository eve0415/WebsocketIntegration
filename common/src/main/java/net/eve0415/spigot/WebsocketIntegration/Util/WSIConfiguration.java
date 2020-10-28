package net.eve0415.spigot.WebsocketIntegration.Util;

public final class WSIConfiguration {
    private String address;
    private int port;

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }
}
