package net.eve0415.spigot.WebsocketIntegration;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import net.eve0415.spigot.WebsocketIntegration.Util.Bootstrap;
import net.eve0415.spigot.WebsocketIntegration.Util.Configuration;
import net.eve0415.spigot.WebsocketIntegration.Util.Logger;
import net.eve0415.spigot.WebsocketIntegration.Util.PlatformType;

public class WebsocketManager {
    private static WebsocketManager instance;
    private static final String IP_REGEX = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";

    // private PlatformType platformtype;
    private Bootstrap bootstrap;
    private Socket socket;
    private boolean isConnected;

    private WebsocketManager(PlatformType platformtype, Bootstrap bootstrap) {
        instance = this;
        // this.platformtype = platformtype;
        this.bootstrap = bootstrap;

        Logger logger = bootstrap.getLogger();
        Configuration configuration = bootstrap.getConfig();

        logger.info("Starting WebsocketIntegration on " + platformtype.getValue() + " ...");

        String address = configuration.getHost();
        int port = configuration.getPort();

        if (address.equals("127.0.0.1")) address = "localhost";
        if (!address.equalsIgnoreCase("localhost") && !address.matches(IP_REGEX)) {
            logger.error("Invalid address cofingured.");
            shutdown();
        }

        if (!(0 <= port && port <= 65535)) {
            logger.error("Invalid port cofingured");
            shutdown();
        }

        try {
            this.socket = IO.socket("http://" + address + ":" + port);
        } catch (URISyntaxException e) {
            logger.error("An error occurred while opening socket", e);
            shutdown();
        }

        new WebsocketEventHandler(this, socket);

        logger.info("Successfully enabled");
    }

    private void shutdown() {
        socket.close();
        socket = null;
        bootstrap.getLogger().info("Shutting down WebsocketIntegration");
    }

    public void isConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public Logger getLogger() {
        return bootstrap.getLogger();
    }

    public Configuration getConfig() {
        return bootstrap.getConfig();
    }

    public static WebsocketManager start(PlatformType platformType, Bootstrap bootstrap) {
        return new WebsocketManager(platformType, bootstrap);
    }

    public static WebsocketManager getInstance() {
        return instance;
    }
}
