package net.eve0415.spigot.WebsocketIntegration;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import net.eve0415.spigot.WebsocketIntegration.WebsocketSender.WebsocketBuilder;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIBootstrap;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfiguration;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.eve0415.spigot.WebsocketIntegration.Util.WSILogger;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType;

public class WebsocketManager {
    private static WebsocketManager instance;
    private static final String IP_REGEX = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";

    private final WSIBootstrap bootstrap;
    private Socket socket;
    private final WebsocketSender sender;
    private boolean isStarting = true;
    private boolean isConnected;

    private WebsocketManager(final WSIBootstrap bootstrap) {
        instance = this;
        this.bootstrap = bootstrap;

        final WSILogger logger = bootstrap.getWSILogger();
        final WSIConfiguration configuration = bootstrap.getWSIConfig();

        logger.info("Starting WebsocketIntegration on " + bootstrap.getPlatformType().getValue() + "...");

        String address = configuration.getAddress();
        final int port = configuration.getPort();

        if (address.equals("127.0.0.1")) address = "localhost";
        if (!address.equalsIgnoreCase("localhost") && !address.matches(IP_REGEX)) {
            logger.error("Invalid address configured.");
            shutdown();
        }

        if (!(0 <= port && port <= 65535)) {
            logger.error("Invalid port configured");
            shutdown();
        }

        try {
            this.socket = IO.socket("http://" + address + ":" + port);
        } catch (final URISyntaxException e) {
            logger.error("An error occurred while opening socket", e);
            shutdown();
        }

        new WebsocketEventHandler(this, socket);
        this.sender = new WebsocketSender(socket);

        socket.connect();

        logger.info("Successfully enabled");
    }

    public void shutdown() {
        if (!isStarting()) {
            try {
                WebsocketManager.getInstance().send(WSIEventState.STOPPING,
                        WebsocketManager.builder().starting(WebsocketManager.getInstance().getPlatformType())
                                .toJSON());
            } catch (final JSONException e) {
                WebsocketManager.getInstance().getWSILogger()
                        .error("There was an error trying to send stopping status.", e);
            }
        }
        socket.close();
        socket = null;

        bootstrap.getWSILogger().info("Shutting down WebsocketIntegration");
    }

    public void isStarting(final boolean isStarting) {
        this.isStarting = isStarting;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void isConnected(final boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public WSIPlatformType getPlatformType() {
        return bootstrap.getPlatformType();
    }

    public int getServerPort() {
        return bootstrap.getServerPort();
    }

    public WSILogger getWSILogger() {
        return bootstrap.getWSILogger();
    }

    public static WebsocketManager start(final WSIBootstrap bootstrap) {
        return new WebsocketManager(bootstrap);
    }

    public static WebsocketManager getInstance() {
        return instance;
    }

    public static WebsocketBuilder builder() {
        return WebsocketSender.builder();
    }

    public void send(final WSIEventState event, final JSONObject content) {
        sender.send(event, content);
    }

    public void handleWebsocketMessage(final String name, final String uuid, final String url, final String message) {
        bootstrap.handleChatMessage(name, uuid, url, message);
    }
}
