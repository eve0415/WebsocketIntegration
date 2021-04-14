package net.eve0415.spigot.WebsocketIntegration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import org.json.JSONException;
import org.slf4j.Logger;

import net.eve0415.spigot.WebsocketIntegration.WebsocketSender.WebsocketBuilder;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfigCache;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfiguration;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIProxy;

@Plugin(id = "websocketintegration", name = "WebsocketIntegration-Velocity", version = "@project.version@", authors = "eve0415")
public class WSIVelocityPlugin implements WSIProxy {
    @Inject
    private ProxyServer proxy;
    @Inject
    private Logger velocitylogger;
    @Inject
    @DataDirectory
    private Path configDir;

    private WSIConfiguration config;
    private WSIVelocityLogger logger;
    private WebsocketManager websocketManager;

    @Override
    public void onEnable() {
        this.logger = new WSIVelocityLogger(velocitylogger);
        try {
            final InputStream defaultConfigLocation = getClass().getResourceAsStream("/config.yml");
            final Path configuration = Paths.get(configDir.toString() + "/config.yml");

            if (!configDir.toFile().exists())
                configDir.toFile().mkdirs();
            if (!configuration.toFile().exists())
                Files.copy(defaultConfigLocation, configuration);

            this.config = WSIConfigCache.readConfig(configuration);
        } catch (final IOException e) {
            logger.error("Failed to copy default config", e);
        }

        this.websocketManager = WebsocketManager.start(this).initialize();
        new WSIVelocityTaskScheduler(this);
        new VelocityEventListner(this);
    }

    @Override
    public void onDisable() {
        websocketManager.shutdown();
    }

    @Override
    public WSIPlatformType getPlatformType() {
        return WSIPlatformType.Velocity;
    }

    @Override
    public WSIConfiguration getWSIConfig() {
        return config;
    }

    @Override
    public WSIVelocityLogger getWSILogger() {
        return logger;
    }

    @Override
    public int getServerPort() {
        return proxy.getBoundAddress().getPort();
    }

    @Override
    public WebsocketManager getWebsocketManager() {
        return websocketManager;
    }

    @Override
    public void handleChatMessage(final String name, final String uuid, final String url, final String message) {
        // Proxy will not handle any message to send to players.
    }

    @Override
    public void sendServerInfo() {
        final WebsocketBuilder data = WebsocketManager.builder();
        proxy.getAllServers().forEach(server -> {
            try {
                data.setServerName(server.getServerInfo().getAddress().getPort(), server.getServerInfo().getName());
            } catch (final JSONException e) {
                logger.error("Could not create server info message", e);
            }
        });
        this.getWebsocketManager().send(WSIEventState.SERVERINFO, data.toJSON());
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onProxyInit(final ProxyInitializeEvent e) {
        onEnable();
    }

    @Subscribe(order = PostOrder.LAST)
    public void onProxyShutdown(final ProxyShutdownEvent e) {
        onDisable();
    }

    public ProxyServer getProxy() {
        return proxy;
    }
}
