package net.eve0415.spigot.WebsocketIntegration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import com.google.inject.Inject;

import org.json.JSONException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIBootstrap;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfigCache;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfiguration;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType;

@Plugin(id = "websocketintegration", name = "WebsocketIntegration-Sponge", version = "@project.version@", authors = "eve0415")
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
    private boolean initialized = false;

    @Override
    public void onEnable() {
        logger = new WSISpongeLogger(spongelogger);

        try {
            final Optional<Asset> config = container.getAsset("config.yml");
            final Path configuration = new File(defaultConfig.getParent() + "/WebsocketIntegration.yml").toPath();

            if (!config.isPresent()) {
                throw new IllegalArgumentException("Default config is missing from jar");
            }

            config.get().copyToFile(configuration);
            this.config = WSIConfigCache.readConfig(configuration);
        } catch (final IOException e) {
            logger.error("Failed to copy default config", e);
        }

        websocketManager = WebsocketManager.start(this);
        chatSender = new WSISpongeChatSender();

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

    @Listener(beforeModifications = true)
    public void onServerLoad(final GameConstructionEvent event) {
        onEnable();
        if (isServerAvailable())
            initialize();
        if (initialized)
            updateStatus(WSIEventState.CONSTRUCTING);
    }

    @Listener(beforeModifications = true)
    public void onServerLoad(final GamePreInitializationEvent event) {
        if (isServerAvailable())
            initialize();
        if (initialized)
            updateStatus(WSIEventState.PreInitialization);
    }

    @Listener(beforeModifications = true)
    public void onServerLoad(final GameInitializationEvent event) {
        if (isServerAvailable())
            initialize();
        if (initialized)
            updateStatus(WSIEventState.Initialization);
    }

    @Listener(beforeModifications = true)
    public void onServerLoad(final GamePostInitializationEvent event) {
        if (isServerAvailable())
            initialize();
        if (initialized)
            updateStatus(WSIEventState.PostInitialization);
    }

    @Listener(beforeModifications = true)
    public void onServerLoad(final GameLoadCompleteEvent event) {
        if (isServerAvailable())
            initialize();
        if (initialized)
            updateStatus(WSIEventState.LoadComplete);
    }

    @Listener(beforeModifications = true)
    public void onServerLoad(final GameAboutToStartServerEvent event) {
        if (isServerAvailable())
            initialize();
        if (initialized)
            updateStatus(WSIEventState.AboutToStart);
    }

    @Listener
    public void onServerStart(final GameStartedServerEvent event) {
        new WSISpongeTaskScheduler(this);
        new SpongeEventListener(this);
    }

    @Listener
    public void onServerStop(final GameStoppingEvent event) {
        onDisable();
    }

    private boolean isServerAvailable() {
        return Sponge.getServer().getBoundAddress().isPresent();
    }

    private void initialize() {
        if (!initialized) {
            websocketManager.initialize();
            initialized = true;
        }
    }

    private void updateStatus(final WSIEventState state) {
        try {
            WebsocketManager.getInstance().send(state, WebsocketManager.builder().basic().toJSON());
        } catch (final JSONException e) {
            WebsocketManager.getInstance().getWSILogger().error("There was an error trying to send starting status.",
                    e);
        }
    }
}
