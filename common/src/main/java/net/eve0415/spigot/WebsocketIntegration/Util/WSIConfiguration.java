package net.eve0415.spigot.WebsocketIntegration.Util;

public class WSIConfiguration {
    private String address;
    private int port;

    public WSIConfiguration() {

    }

    public WSIConfiguration(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
