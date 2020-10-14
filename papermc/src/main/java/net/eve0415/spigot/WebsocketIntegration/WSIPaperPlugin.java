package net.eve0415.spigot.WebsocketIntegration;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIBootstrap;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfigCache;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIConfiguration;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType;

public class WSIPaperPlugin extends JavaPlugin implements WSIBootstrap {
    private WSIConfiguration config;
    private WSIPaperLogger logger;
    private WebsocketManager websocketManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.config = WSIConfigCache.readConfig(new File(getDataFolder().toString() + "/config.yml").toPath());
        this.logger = new WSIPaperLogger(getLogger());
        this.websocketManager = WebsocketManager.start(WSIPlatformType.Paper, this);

        new WSITaskScheduler(this);
    }

    @Override
    public void onDisable() {
    }

    public WSIConfiguration getWSIConfig() {
        return config;
    }

    public WSIPaperLogger getWSILogger() {
        return logger;
    }

    public WebsocketManager getWebsocketManager() {
        return websocketManager;
    }
}
