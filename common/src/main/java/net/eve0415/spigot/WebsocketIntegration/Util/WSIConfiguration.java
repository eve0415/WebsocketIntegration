package net.eve0415.spigot.WebsocketIntegration.Util;

public final class WSIConfiguration {
    private String address;
    private int port;
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }
}
