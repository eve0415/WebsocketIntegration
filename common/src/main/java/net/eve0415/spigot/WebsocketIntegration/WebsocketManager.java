package net.eve0415.spigot.WebsocketIntegration;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIBootstrap;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfiguration;
import net.eve0415.spigot.WebsocketIntegration.Util.WSILogger;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType;

public class WebsocketManager {
    private static WebsocketManager instance;
    private static final String IP_REGEX = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";

    // private PlatformType platformtype;
    private WSIBootstrap bootstrap;
    private Socket socket;
    private boolean isConnected;

    private WebsocketManager(WSIPlatformType platformtype, WSIBootstrap bootstrap) {
        instance = this;
        // this.platformtype = platformtype;
        this.bootstrap = bootstrap;

        WSILogger logger = bootstrap.getWSILogger();
        WSIConfiguration configuration = bootstrap.getWSIConfig();

        logger.info("Starting WebsocketIntegration on " + platformtype.getValue() + " ...");

        String address = configuration.getAddress();
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

        socket.connect();

        logger.info("Successfully enabled");
    }

    private void shutdown() {
        socket.close();
        socket = null;
        bootstrap.getWSILogger().info("Shutting down WebsocketIntegration");
    }

    public void isConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public WSILogger getWSILogger() {
        return bootstrap.getWSILogger();
    }

    public static WebsocketManager start(WSIPlatformType platformType, WSIBootstrap bootstrap) {
        return new WebsocketManager(platformType, bootstrap);
    }

    public static WebsocketManager getInstance() {
        return instance;
    }
}
