package net.eve0415.spigot.WebsocketIntegration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIBootstrap;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfigCache;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfiguration;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType;

@Plugin(id = "websocketintegration", name = "WebsocketIntegration-Velocity", version = "@project.version@", authors = "eve0415")
public class WSIVelocityPlugin implements WSIBootstrap {
    @Inject
    private ProxyServer proxy;
    @Inject
    private Logger velocitylogger;
    @Inject
    @DataDirectory
    private Path configDir;

    // private final ProxyServer instance;
    private WSIConfiguration config;
    private WSIVelocityLogger logger;
    private WebsocketManager websocketManager;
    // private WSISpongeChatSender chatSender;

    @Override
    public void onEnable() {
        this.logger = new WSIVelocityLogger(velocitylogger);
        try {
            final InputStream defaultConfigLocation = getClass().getResourceAsStream("/config.yml");
            final Path configuration = Paths.get(configDir.toString() + "/config.yml");

            if (!configDir.toFile().exists()) configDir.toFile().mkdirs();
            if (!configuration.toFile().exists()) Files.copy(defaultConfigLocation, configuration);

            this.config = WSIConfigCache.readConfig(configuration);
        } catch (final IOException e) {
            logger.error("Failed to copy default config", e);
        }

        this.websocketManager = WebsocketManager.start(this);
        new WSIVelocityTaskScheduler(this);
    }

    @Override
    public void onDisable() {
        websocketManager.shutdown();
    }

    public ProxyServer getProxy() {
        return proxy;
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
        return 25565; // I couldn't find a way to get port address so I'll just return 25565 for now.
    }

    @Override
    public WebsocketManager getWebsocketManager() {
        return websocketManager;
    }

    @Override
    public void handleChatMessage(final String name, final String uuid, final String url, final String message) {
        // Proxy will not handle any message to send to players.
    }

    @Subscribe
    public void onProxyInit(final ProxyInitializeEvent e) {
        onEnable();
    }

    @Subscribe
    public void onProxyShutdown(final ProxyShutdownEvent e) {
        onDisable();
    }
}
