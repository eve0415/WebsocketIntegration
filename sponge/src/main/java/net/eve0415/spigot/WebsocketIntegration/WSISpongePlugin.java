package net.eve0415.spigot.WebsocketIntegration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIBootstrap;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfigCache;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfiguration;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType;

@Plugin(id = "websocketintegration", name = "WebsocketIntegration-Sponge", version = "1.1-SNAPSHOT", authors = "eve0415")
public class WSISpongePlugin implements WSIBootstrap {
    @Inject
    private Logger spongelogger;
    @Inject
    private PluginContainer container;
    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    private WSIConfiguration config;
    private WSISpongeLogger logger;
    private WebsocketManager websocketManager;
    private WSISpongeChatSender chatSender;

    @Override
    public void onEnable() {
        this.logger = new WSISpongeLogger(spongelogger);

        try {
            final Optional<Asset> config = container.getAsset("config.yml");
            final Path configuration = new File(defaultConfig.getParent() + "/WebsocketIntegration.yml")
                    .toPath();

            if (!config.isPresent()) {
                throw new IllegalArgumentException("Default config is missing from jar");
            }

            config.get().copyToFile(configuration);
            this.config = WSIConfigCache.readConfig(configuration);
        } catch (final IOException e) {
            logger.error("Failed to copy default config", e);
        }

        this.websocketManager = WebsocketManager.start(this);
        this.chatSender = new WSISpongeChatSender();

    }

    @Override
    public void onDisable() {
        websocketManager.shutdown();
    }

    @Override
    public WSIPlatformType getPlatformType() {
        return WSIPlatformType.Sponge;
    }

    @Override
    public WSIConfiguration getWSIConfig() {
        return config;
    }

    @Override
    public WSISpongeLogger getWSILogger() {
        return logger;
    }

    @Override
    public int getServerPort() {
        return Sponge.getServer().getBoundAddress().get().getPort();
    }

    @Override
    public WebsocketManager getWebsocketManager() {
        return websocketManager;
    }

    @Override
    public void handleChatMessage(final String name, final String uuid, final String url, final String message) {
        chatSender.chatHandler(name, uuid, url, message);
    }

    @Listener
    public void onServerLoad(final GameInitializationEvent event) {
        onEnable();
    }

    @Listener
    public void onServerStart(final GameAboutToStartServerEvent event) {
        new WSISpongeTaskScheduler(this);
        new SpongeEventListener(this);
    }

    @Listener
    public void onServerStop(final GameStoppedEvent event) {
        onDisable();
    }
}
